package work.pcdd.blogApi.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.xiaoymin.knife4j.annotations.ApiSort;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import work.pcdd.blogApi.common.annotation.RequiresRoles;
import work.pcdd.blogApi.common.vo.Result;
import work.pcdd.blogApi.entity.LoginLog;
import work.pcdd.blogApi.service.LoginLogService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 1907263405@qq.com
 * @date 2021/4/15 0:21
 */
@Api(tags = "登录日志管理")
@ApiSort(4)
@CacheConfig(cacheNames = "login_log")
@RestController
@RequestMapping("/login/log")
public class LoginLogController {

    @Autowired
    LoginLogService loginLogService;

    @ApiOperation("分页查询所有登录日志")
    @RequiresRoles(role = "admin")
    @Cacheable(key = "getMethodName()+ '?current='+#currentPage+'&page='+#pageSize")
    @GetMapping("/list")
    public Result list(@ApiParam("从第几条开始查询，默认值1") @RequestParam(defaultValue = "1", name = "current") Integer currentPage
            , @ApiParam("每页显示的条数，默认值10") @RequestParam(defaultValue = "10", name = "page") Integer pageSize) {

        Page<LoginLog> page = new Page<>(currentPage, pageSize);
        IPage<LoginLog> data = loginLogService.page(page
                , new QueryWrapper<LoginLog>().orderByDesc("login_datetime"));
        Assert.notNull(data,"登录日志为空");

        return Result.success(data);
    }

    @ApiOperation("根据用户id查询登录日志")
    @RequiresRoles(role = "user")
    @Cacheable(key = "getMethodName()+ '/'+#id")
    @GetMapping("/{id}")
    public Result findById(@ApiParam("用户id") @PathVariable Long id) {
        List<LoginLog> loginLog = loginLogService.list(new QueryWrapper<LoginLog>().eq("user_id", id));
        Assert.notEmpty(loginLog, "登录日志不存在");
        return Result.success(loginLog);
    }

    @ApiOperation("删除最近几天的登录日志")
    @RequiresRoles(role = "admin")
    @CacheEvict(key = "getMethodName()+ '/'+#id", allEntries = true)
    @DeleteMapping("/{day}")
    public Result clear(@ApiParam("天数") @PathVariable Integer day) {
        int count = loginLogService.deleteByDay(day);
        Assert.isTrue(count > 0, "最近" + day + "天无登录日志，无法删除");
        return Result.success(count);
    }


}
