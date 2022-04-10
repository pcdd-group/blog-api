package work.pcdd.blogapi.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import work.pcdd.blogapi.entity.Blog;
import work.pcdd.blogapi.mapper.BlogMapper;
import work.pcdd.blogapi.service.BlogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 1907263405@qq.com
 * @since 2021-02-26
 */
@Service
public class BlogServiceImpl extends ServiceImpl<BlogMapper, Blog> implements BlogService {

    @Autowired
    BlogMapper blogMapper;

    @Override
    public List<Blog> findAll() {
        return blogMapper.findAll();
    }

}
