package work.pcdd.blogapi.controller;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import work.pcdd.blogapi.common.annotation.RequiresRoles;
import work.pcdd.blogapi.common.constant.CommonConst;
import work.pcdd.blogapi.common.vo.Result;
import work.pcdd.blogapi.entity.Blog;
import work.pcdd.blogapi.service.BlogService;
import work.pcdd.blogapi.service.UserService;
import work.pcdd.blogapi.util.JwtUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * CacheConfig注解用于设置类中所有方法的缓存名（通过cacheNames属性），
 * 如果在类上标注了@CacheConfig，则方法@Cacheable的value属性可以不用设置，
 * 反之，必须设置缓存名
 *
 * @author 1907263405@qq.com
 * @since 2021-02-26
 */
@Slf4j
@Api(tags = "文章管理")
@ApiSort(3)
@RestController
@RequestMapping("/blog")
@CacheConfig(cacheNames = "blog")
public class BlogController {

    @Autowired
    BlogService blogService;
    @Autowired
    UserService userService;
    @Autowired
    JwtUtils jwtUtils;

    @ApiOperation("分页查询所有文章")
    @ApiOperationSupport(order = 1)
    @GetMapping("/list")
    @Cacheable(key = "'/'+getMethodName()+'?current='+#currentPage+'&size='+#pageSize")
    public Result list(@ApiParam("从第几条开始查询，默认值1") @RequestParam(defaultValue = "1", name = "current") Integer currentPage
            , @ApiParam("每页显示的条数，默认10") @RequestParam(defaultValue = "10", name = "page") Integer pageSize) {

        Page<Blog> page = new Page<>(currentPage, pageSize);
        IPage<Blog> data = blogService.page(page, new QueryWrapper<Blog>().orderByDesc("created"));

        return Result.ok(data);
    }

    @ApiOperation("根据id查询文章")
    @ApiOperationSupport(order = 2)
    @GetMapping("/{id}")
    @Cacheable(key = "getMethodName()+'/'+#id")
    public Result findById(@ApiParam("文章id") @PathVariable Long id) {
        log.info("==========请求数据库==========");
        Blog blog = blogService.getById(id);
        Assert.notNull(blog, "文章不存在");
        return Result.ok(blog);
    }

    @ApiOperation("根据文章标题模糊查询文章")
    @ApiOperationSupport(order = 3)
    @RequiresRoles(role = "user")
    @GetMapping("/title/{title}")
    @Cacheable(key = "getMethodName()+'/'+#title")
    public Result findByTitle(@ApiParam("文章标题") @PathVariable String title) {
        log.info(title);
        QueryWrapper<Blog> qw = new QueryWrapper<>();
        List<Blog> list = blogService.list(qw.like("title", title));
        Assert.notEmpty(list, "文章不存在");
        return Result.ok(list);
    }

    @ApiOperation("发布文章")
    @ApiOperationSupport(order = 4, ignoreParameters = {"created", "id", "status"})
    @RequiresRoles(role = "user")
    @CacheEvict(allEntries = true)
    @PostMapping("/add")
    public Result add(@Validated @RequestBody Blog blog, @RequestHeader(CommonConst.TOKEN_HEADER) String token) {
        String userId = jwtUtils.parseToken(token);
        // 只能发布自己的的文章：判断token中的serId和请求体中的userId是否一致
        Assert.isTrue(userId.equals(blog.getUserId().toString()), "非法操作");

        // 先判断用户是否存在，存在才可以发布文章
        Assert.notNull(userService.getById(blog.getUserId()), "用户不存在，无法发布文章");

        blog.setCreated(LocalDateTime.now());
        return Result.ok(blogService.save(blog));
    }

    /**
     * CachePut：这里的key代表要更新的缓存名称，
     * key必须和Cacheable中的key保持一致，否则无法更新对应的缓存
     * 注意格式：返回结果 作为新缓存
     */
    @ApiOperation("更新文章")
    @ApiOperationSupport(order = 5)
    @RequiresRoles(role = "user")
    @PutMapping("/edit")
    @CacheEvict(key = "'findById/'+#blog.id", allEntries = true)
    public Result updateById(@Validated @RequestBody Blog blog, @RequestHeader(CommonConst.TOKEN_HEADER) String token) {
        String userId = jwtUtils.parseToken(token);
        // 只能更新自己的的文章：判断token中的serId和请求体中的userId是否一致
        Assert.isTrue(userId.equals(blog.getUserId().toString()), "非法操作");

        Assert.notNull(blogService.getById(blog.getId()), "文章不存在或已被删除");

        Assert.isTrue(blogService.update(blog, new UpdateWrapper<Blog>()
                .set("title", blog.getTitle())
                .set("description", blog.getDescription())
                .set("content", blog.getContent())
                .eq("user_id", userId)
                .eq("id", blog.getId())), "非法操作");

        return Result.ok(blog);
    }

    /**
     * CacheEvict：这里的key代表要删除的缓存名称，
     * key必须和Cacheable中的key保持一致，否则无法删除对应的缓存
     */
    @ApiOperation("根据id删除文章")
    @ApiOperationSupport(order = 6)
    @RequiresRoles(role = "user")
    @DeleteMapping("/{id}")
    @CacheEvict(key = "'findById/'+#id", allEntries = true)
    public Result deleteById(@ApiParam("文章id") @PathVariable Long id) {
        Assert.notNull(blogService.getById(id), "文章不存在，无法删除");
        return Result.ok(blogService.removeById(id));
    }


}
