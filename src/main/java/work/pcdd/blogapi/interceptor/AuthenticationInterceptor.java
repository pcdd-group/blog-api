package work.pcdd.blogapi.interceptor;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import work.pcdd.blogapi.common.annotation.RequiresRoles;
import work.pcdd.blogapi.common.exception.MyUnauthenticatedException;
import work.pcdd.blogapi.common.exception.MyUnauthorizedException;
import work.pcdd.blogapi.entity.User;
import work.pcdd.blogapi.mapper.UserMapper;
import work.pcdd.blogapi.service.UserService;
import work.pcdd.blogapi.util.JwtUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * 权限拦截器
 *
 * @author 1907263405@qq.com
 * @date 2021/3/6 23:24
 */
@Slf4j
public class AuthenticationInterceptor implements HandlerInterceptor {

    @Autowired
    UserService userService;
    @Autowired
    UserMapper userMapper;
    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    RedisTemplate<String, Object> redisTemplate;
    @Value("${pcdd.tokenHeader}")
    private String tokenHead;
    @Value("${pcdd.secret}")
    private String secret;

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse resp, Object handler) {
        // 如果不是映射到方法直接通过
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        String token = req.getHeader(tokenHead);
        log.info("ip：{}，uri：{}，token：{}", req.getRemoteAddr(), req.getRequestURI(), token);

        // 方法有鉴权注解，进行认证
        if (method.isAnnotationPresent(RequiresRoles.class)) {

            // 请求头中无token，是匿名用户
            if (!StringUtils.hasText(token)) {
                throw new MyUnauthenticatedException("未登录，请先登录");
            }

            // 校验JWT的合法性，若合法则返回JWT中的userId
            String id = jwtUtils.parseToken(token);

            // 从数据库中查找token中的id，若不存在，说明该用户被删除
            Assert.isTrue(userMapper.selectCount(new QueryWrapper<User>().eq("id", id)) == 1, "用户不存在，请重新登录");

            // 从redis中查找token中的id，若不存在，说明该用户被强制下线或缓存已过期(或被删除)
            Assert.notNull(redisTemplate.opsForValue().get(id), "登录已过期，请重新登录");

            RequiresRoles annotation = method.getAnnotation(RequiresRoles.class);

            // 鉴于目前的角色只有两种：user、admin，所以这里使用三元
            String annotationRole = "user".equals(annotation.role()) ? "user" : "admin";

            // 查询用户的真实角色
            String realRole = userService.getOne(new QueryWrapper<User>()
                    .eq("id", id).select("role")).getRole();

            // 是管理员，无论role为user还是admin，均直接通过
            if ("admin".equals(realRole)) {
                log.info("admin授权成功！");
                return true;
            }

            // 若普通用户调用admin接口，则抛出403异常
            if (!("user".equals(realRole) && "user".equals(annotationRole))) {
                throw new MyUnauthorizedException("权限不足");
            }

            log.info("user授权成功！");
            return true;
        }

        // 接口无需鉴权，直接放行
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }

}
