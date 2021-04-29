package work.pcdd.blogApi.mapper;

import org.springframework.stereotype.Repository;
import work.pcdd.blogApi.entity.LoginLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 1907263405@qq.com
 * @since 2021-04-15
 */
@Repository
public interface LoginLogMapper extends BaseMapper<LoginLog> {
    int deleteByDay(int day);
}
