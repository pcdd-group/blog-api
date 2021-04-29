package work.pcdd.blogApi.controller;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import com.auth0.jwt.JWT;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSort;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import work.pcdd.blogApi.common.annotation.RequiresRoles;
import work.pcdd.blogApi.common.vo.Result;
import work.pcdd.blogApi.common.dto.LoginDto;
import work.pcdd.blogApi.common.dto.ProfileDto;
import work.pcdd.blogApi.entity.LoginLog;
import work.pcdd.blogApi.entity.User;
import work.pcdd.blogApi.service.LoginLogService;
import work.pcdd.blogApi.service.UserService;
import work.pcdd.blogApi.util.JwtUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 登录控制器
 *
 * @author 1907263405@qq.com
 * @date 2021/3/6 23:07
 */
@Api(tags = "权限管理")
@ApiSort(1)
@RestController
public class AccountController {

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    UserService userService;

    @Autowired
    LoginLogService loginLogService;

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    @Value("${pcdd.tokenHead}")
    private String tokenHead;

    @Value("${pcdd.refresh-token-expire}")
    private Long expire;

    @Value("${pcdd.salt}")
    private String salt;

    @ApiOperation("用户登录")
    @ApiOperationSupport(order = 1)
    @PostMapping("/login")
    public Result login(@Validated @RequestBody LoginDto loginDto
            , HttpServletRequest request
            , HttpServletResponse response) {

        System.out.println("登录Controller执行！");

        // 根据用户名查询userId
        User user = userService.getOne(new QueryWrapper<User>()
                .eq("username", loginDto.getUsername())
                .select("id"));

        // 判断用户名是否正确
        Assert.notNull(user, "用户名或密码错误");

        // 防止用户重复登录
        Assert.isTrue(redisTemplate.opsForValue().get(String.valueOf(user.getId())) == null
                , "你已登录，请勿重复登录");

        QueryWrapper<User> queryWrapper = new QueryWrapper<User>()
                .eq("username", loginDto.getUsername())
                .eq("password", SecureUtil.md5(salt + loginDto.getPassword() + salt));
        user = userService.getOne(queryWrapper);

        // 判断密码是否正确
        Assert.notNull(user, "用户名或密码错误");

        // 判断用户是否被锁定
        Assert.isTrue(user.getStatus() != -1, "该用户已被锁定");
        // 判断用户是否被禁用
        Assert.isTrue(user.getStatus() != 0, "该用户已被禁用");

        // 用户名密码匹配成功，开始生成token
        String token = jwtUtils.createToken(user);

        LocalDateTime now = LocalDateTime.now();

        // 将userId作为key、最后一次登录时间作为value存入redis，并设置有效期，作为用户是否登录凭证
        redisTemplate.opsForValue().set(String.valueOf(user.getId())
                , DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(now)
                , expire, TimeUnit.DAYS);

        ProfileDto profileDto = new ProfileDto();
        // 将user对象的属性拷贝到profileDto对象中
        BeanUtils.copyProperties(user, profileDto);

        // 生成登录日志
        LoginLog loginLog = new LoginLog();
        UserAgent ua = UserAgentUtil.parse(request.getHeader("user-agent"));
        loginLog.setUserId(user.getId());
        loginLog.setLoginDatetime(now);
        loginLog.setIpAddr(request.getRemoteAddr());
        loginLog.setBrowser(ua.getBrowser().getName() + "/" + ua.getVersion());
        loginLog.setEngine(ua.getEngine() + "/" + ua.getEngineVersion());
        loginLog.setOs(ua.getOs().getName());
        loginLog.setIsMobile(ua.isMobile());
        Assert.isTrue(loginLogService.save(loginLog), "登录日志保存失败");

        // 更新用户最后一次登录时间，要写在对象拷贝之后，否则日期是本次的登录时间
        userService.update(new UpdateWrapper<>(user).eq("id", user.getId()).set("last_login", now));

        response.setHeader(tokenHead, token);
        response.setHeader("Access-Control-Expose-Headers", tokenHead);

        return Result.success(profileDto);
    }

    @ApiOperation("退出登录")
    @ApiOperationSupport(order = 2)
    @RequiresRoles(role = "user")
    @GetMapping("/logout")
    public Result logout(HttpServletRequest request) {
        String token = request.getHeader(tokenHead);
        // 删除缓存
        redisTemplate.delete(JWT.decode(token).getAudience().get(0));
        return Result.success();
    }

    /**
     * 刷新token，每当用户打开主页时，发送携带token的请求，
     * 后端判断token的合法性，若通过校验，返回新的token以及最新的用户信息
     */
    @ApiOperation("刷新token")
    @ApiOperationSupport(order = 3)
    @RequiresRoles(role = "user")
    @GetMapping("/refreshToken")
    public Result refreshToken(HttpServletRequest req, HttpServletResponse resp) {
        String token = req.getHeader("Authorization");

        // 校验JWT的合法性，若合法则返回JWT中的userId
        String userId = jwtUtils.parseToken(token);

        User user = userService.getById(userId);

        // 假如用户的账号在访问服务期间被删除了
        Assert.notNull(user, "用户不存在");

        // 假如用户的账号在访问服务期间被锁定了
        Assert.isTrue(user.getStatus() != -1, "用户已被锁定");

        // 若token、用户的状态均正常，则生成新的token返回到前端
        String jwt = jwtUtils.createToken(user);
        resp.setHeader("Authorization", jwt);
        resp.setHeader("Access-Control-Expose-Headers", "Authorization");

        // 更新redis中该用户最后的在线时间
        redisTemplate.opsForValue().set(userId
                , DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now())
                , expire, TimeUnit.DAYS);

        Map<String, Object> map = new HashMap<>(16);
        map.put("id", user.getId());
        map.put("username", user.getUsername());
        map.put("avatar", user.getAvatar());
        map.put("email", user.getEmail());

        return Result.success(map);
    }

}
