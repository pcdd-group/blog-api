package work.pcdd.blogapi.controller;

import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSort;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import work.pcdd.blogapi.common.annotation.RequiresRoles;
import work.pcdd.blogapi.common.constant.CommonConst;
import work.pcdd.blogapi.common.dto.ProfileDto;
import work.pcdd.blogapi.common.vo.Result;
import work.pcdd.blogapi.entity.User;
import work.pcdd.blogapi.service.UserService;
import work.pcdd.blogapi.util.JwtUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 1907263405@qq.com
 * @since 2021-02-26
 */
@Slf4j
@Api(tags = "用户管理")
@ApiSort(2)
@RestController
@RequestMapping("/user")
@CacheConfig(cacheNames = "user")
public class UserController {

    @Autowired
    UserService userService;
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    JwtUtils jwtUtils;
    @Value("${pcdd.salt}")
    private String salt;

    @ApiOperation("分页查询所有用户")
    @ApiOperationSupport(order = 1)
    @RequiresRoles(role = "admin")
    @GetMapping("/list")
    @Cacheable(key = "getMethodName()+'?currentPage='+#currentPage+'&pageSize='+#pageSize")
    public Result list(@ApiParam("从第几条开始查询，默认值1") @RequestParam(defaultValue = "1", name = "current") Integer currentPage
            , @ApiParam("每页显示的条数，默认值10") @RequestParam(defaultValue = "10", name = "page") Integer pageSize) {

        log.info("请求数据库");
        List<User> list = userService.list();
        Assert.notNull(list, "用户列表为空");

        Page<User> page = new Page<>(currentPage, pageSize);
        IPage<User> data = userService.page(page, new QueryWrapper<>());
        return Result.ok(data);
    }

    @ApiOperation("根据id查询用户")
    @ApiOperationSupport(order = 2)
    @RequiresRoles(role = "admin")
    @GetMapping("/{id}")
    @Cacheable(key = "getMethodName()+'/'+#id")
    public Result findById(@ApiParam("用户id") @PathVariable String id) {
        log.info("无缓存，请求数据库");
        User user = userService.getById(id);
        Assert.notNull(user, "用户不存在");
        return Result.ok(user);
    }

    @ApiOperation("修改个人信息")
    @ApiOperationSupport(order = 3, ignoreParameters = {"created", "lastLogin"})
    @RequiresRoles(role = "user")
    @PutMapping("/edit}")
    @CachePut(key = "'findById'+#profileDto.id")
    public Result edit(@Validated @RequestBody ProfileDto profileDto, @RequestHeader(CommonConst.TOKEN_HEADER) String token) {
        String userId = jwtUtils.parseToken(token);
        // 只能修改自己的信息：判断token中的serId和请求体中的userId是否一致，一致说明修改的是本人信息
        Assert.isTrue(userId.equals(profileDto.getId().toString()), "非法操作");
        boolean update = userService.update(new UpdateWrapper<User>()
                .set("username", profileDto.getUsername())
                .set("avatar", profileDto.getAvatar())
                .set("email", profileDto.getEmail())
                .eq("id", profileDto.getId()));
        Assert.isTrue(update, "修改用户信息失败");
        return Result.ok();
    }

    @ApiOperation("根据id删除用户")
    @ApiOperationSupport(order = 4)
    @RequiresRoles(role = "admin")
    @DeleteMapping("/{id}")
    @CacheEvict(key = "'findById'+#id", allEntries = true)
    public Result deleteById(@ApiParam("用户id") @PathVariable String id) {
        Assert.notNull(userService.getById(id), "用户不存在，无法删除");
        Assert.isTrue(userService.removeById(id), "删除用户失败");
        return Result.ok();
    }

    @ApiOperation("用户注册")
    @ApiOperationSupport(order = 5, includeParameters = {"username", "email", "avatar", "password"})
    @PostMapping("/register")
    public Result register(@Validated @RequestBody User user) {
        // 判断用户名是否存在
        Assert.isTrue(userService.getOne(new QueryWrapper<User>()
                .select("username").eq("username", user.getUsername())) == null, "用户名已存在");

        // 判断邮箱是否已注册
        Assert.isTrue(userService.getOne(new QueryWrapper<User>()
                .select("email").eq("email", user.getEmail())) == null, "该邮箱已注册");

        user.setCreated(LocalDateTime.now());

        // 用户密码加密
        user.setPassword(SecureUtil.md5(salt + user.getPassword() + salt));

        // 判断insert影响的行数是否为0
        Assert.isTrue(userService.save(user), "注册失败");

        ProfileDto profileDto = new ProfileDto();
        BeanUtils.copyProperties(user, profileDto);
        return Result.ok(profileDto);
    }

    @ApiOperation("根据用户id锁定用户")
    @ApiOperationSupport(order = 6)
    @RequiresRoles(role = "admin")
    @PostMapping("/lock/{id}")
    public Result lockUser(@ApiParam("用户id") @PathVariable String id) {
        Assert.notNull(userService.getById(id), "用户不存在，无法锁定");
        Assert.isTrue(userService.update(new UpdateWrapper<User>().set("status", 0).
                eq("id", id)), "锁定失败");
        return Result.ok();
    }

    @ApiOperation("根据用户id解锁用户")
    @ApiOperationSupport(order = 7)
    @RequiresRoles(role = "admin")
    @PostMapping("/unlock/{id}")
    public Result unLockUser(@ApiParam("用户id") @PathVariable String id) {
        Assert.notNull(userService.getById(id), "用户不存在，无法解锁");
        Assert.notNull(userService.getOne(new QueryWrapper<User>()
                .eq("id", id).eq("status", 0)), "该账户未被锁定，无法解锁");
        Assert.isTrue(userService.update(new UpdateWrapper<User>().set("status", 1).
                eq("id", id)), "解锁失败");
        return Result.ok();
    }


}
