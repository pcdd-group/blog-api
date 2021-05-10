package work.pcdd.blogApi.aop;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import work.pcdd.blogApi.entity.SysLog;
import work.pcdd.blogApi.entity.User;
import work.pcdd.blogApi.service.ISysLogService;
import work.pcdd.blogApi.service.UserService;
import work.pcdd.blogApi.util.JwtUtils;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * @author 1907263405@qq.com
 * @date 2021/5/10 15:41
 */
@Slf4j
@Aspect
@Component
public class ApiLogAspect {

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    ISysLogService sysLogService;

    @Autowired
    UserService userService;

    private ThreadLocal<Long> startTime = new ThreadLocal<>();

    //@Pointcut("execution(public * work.pcdd.blogApi.controller.*.*(..))")
    @Pointcut("@annotation(io.swagger.annotations.ApiOperation)")
    private void apiLog() {
    }

    @Before("apiLog()")
    public void doBefore(JoinPoint jp) {
        System.out.println("=====================doBefore======================");
        log.info("前置增强");
        System.out.println("=====================doBefore======================");
    }

    @AfterReturning(pointcut = "apiLog()", returning = "result")
    public void doAfterReturning(Object result) {
        System.out.println("=====================doAfterReturning======================");
        log.info("后置增强");
        System.out.println("=====================doAfterReturning======================");
    }

    @Around("apiLog()")
    public Object doAround(ProceedingJoinPoint pjp) {
        System.out.println("=====================doAround======================");
        SysLog sysLog = new SysLog();
        startTime.set(System.currentTimeMillis());

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        String token = request.getHeader("Authorization2");
        log.info("-----------------token:{}", token);

        if (StringUtils.hasText(token)) {
            String userId = jwtUtils.parseToken(token);
            // 用户名
            sysLog.setUsername(userService.getOne(new QueryWrapper<User>()
                    .eq("id", userId)
                    .select("username"))
                    .getUsername());
        } else {
            sysLog.setUsername("访客");
        }

        // 获取ApiOperation的value，作为用户操作
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        ApiOperation annotation = signature.getMethod().getAnnotation(ApiOperation.class);
        sysLog.setOperation(annotation.value());

        // 请求方法
        sysLog.setMethod(request.getMethod());
        // 请求路径
        sysLog.setRequestUrl(request.getRequestURL().toString());
        // 方法参数
        sysLog.setParams(Arrays.toString(pjp.getArgs()));
        // IP地址
        sysLog.setIp(request.getRemoteAddr());
        // 请求时间
        sysLog.setCreateDate(LocalDateTime.now());

        try {
            Object obj = pjp.proceed();
            log.info("{}", System.currentTimeMillis() - startTime.get());
            sysLog.setTime(System.currentTimeMillis() - startTime.get());

            log.info("{}", sysLog);
            sysLogService.save(sysLog);
            System.out.println("=====================doAround======================");
            return obj;

        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }

    }

}
