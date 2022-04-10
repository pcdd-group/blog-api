package work.pcdd.blogapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import work.pcdd.blogapi.interceptor.AuthenticationInterceptor;

/**
 * @author 1907263405@qq.com
 * @date 2021/3/6 23:09
 */
@Configuration
public class MvcConfig implements WebMvcConfigurer {

    /**
     * SpringMVC拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authenticationInterceptor())
                // 拦截所有路径
                .addPathPatterns("/**");
    }

    /**
     * 配置跨域
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 指哪些接口URL需要增加跨域设置
        registry.addMapping("/**")
                // 前端哪些域名被允许跨域
                .allowedOriginPatterns("*")
                // 允许哪些请求header访问
                .allowedHeaders("*")
                // 允许哪些方法
                .allowedMethods("GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS")
                // 意义是允许客户端携带验证信息，例如 cookie 之类的
                .allowCredentials(true)
                .maxAge(3600);
    }

    @Bean
    public AuthenticationInterceptor authenticationInterceptor() {
        return new AuthenticationInterceptor();
    }

}
