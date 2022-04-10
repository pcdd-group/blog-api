package work.pcdd.blogapi.controller;


import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.xiaoymin.knife4j.annotations.ApiSort;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import work.pcdd.blogapi.common.annotation.RequiresRoles;
import work.pcdd.blogapi.common.vo.Result;
import work.pcdd.blogapi.entity.SysLog;
import work.pcdd.blogapi.service.ISysLogService;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 1907263405@qq.com
 * @since 2021-05-10
 */
@Api(tags = "系统日志管理")
@ApiSort(5)
@RestController
@RequestMapping("/sys-log")
public class SysLogController {

    @Autowired
    ISysLogService sysLogService;

    @ApiOperation("分页查询所有日志")
    @RequiresRoles(role = "admin")
    @GetMapping("/list")
    public Result list(@ApiParam("从第几条开始查询，默认值1") @RequestParam(name = "current", defaultValue = "1") Integer currentPage
            , @ApiParam("每页显示的条数，默认值10") @RequestParam(name = "page", defaultValue = "10") Integer pageSize) {

        Page<SysLog> page = new Page<>(currentPage, pageSize);
        IPage<SysLog> data = sysLogService.page(page, new QueryWrapper<SysLog>().orderByDesc("create_date"));
        Assert.notNull(data, "日志列表为空");

        return Result.ok(data);
    }

    @ApiOperation("删除指定行数的最早的系统日志")
    @RequiresRoles(role = "admin")
    @DeleteMapping("/{row}")
    public Result deleteByRow(@ApiParam("删除的行数") @PathVariable Integer row) {
        Assert.isTrue(sysLogService.remove(new QueryWrapper<SysLog>().last("limit " + row)), "删除系统日志失败");
        return Result.ok("成功删除" + row + "行系统日志记录");
    }

}
