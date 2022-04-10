package work.pcdd.blogapi.service;

import work.pcdd.blogapi.entity.Blog;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 1907263405@qq.com
 * @since 2021-02-26
 */

public interface BlogService extends IService<Blog> {
    List<Blog> findAll();
}
