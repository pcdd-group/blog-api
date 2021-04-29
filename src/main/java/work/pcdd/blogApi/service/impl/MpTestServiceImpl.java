package work.pcdd.blogApi.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import work.pcdd.blogApi.entity.MpTest;
import work.pcdd.blogApi.mapper.MpTestMapper;
import work.pcdd.blogApi.service.IMpTestService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 1907263405@qq.com
 * @since 2021-04-15
 */
@Service
public class MpTestServiceImpl extends ServiceImpl<MpTestMapper, MpTest> implements IMpTestService {

}
