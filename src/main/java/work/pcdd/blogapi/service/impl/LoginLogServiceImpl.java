package work.pcdd.blogapi.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import work.pcdd.blogapi.entity.LoginLog;
import work.pcdd.blogapi.mapper.LoginLogMapper;
import work.pcdd.blogapi.service.LoginLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 1907263405@qq.com
 * @since 2021-04-15
 */
@Service
public class LoginLogServiceImpl extends ServiceImpl<LoginLogMapper, LoginLog> implements LoginLogService {

    @Autowired
    LoginLogMapper loginLogMapper;

    @Override
    public int deleteByDay(int day) {
        return loginLogMapper.deleteByDay(day);
    }

}
