package work.pcdd.blogapi.service.impl;

import work.pcdd.blogapi.entity.User;
import work.pcdd.blogapi.mapper.UserMapper;
import work.pcdd.blogapi.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 1907263405@qq.com
 * @since 2021-02-26
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
