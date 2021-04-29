package work.pcdd.blogApi.mapper;

import org.springframework.stereotype.Repository;
import work.pcdd.blogApi.entity.Blog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author 1907263405@qq.com
 * @since 2021-02-26
 */
@Repository
public interface BlogMapper extends BaseMapper<Blog> {
    List<Blog> findAll();
}
