package work.pcdd.blogapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * Swagger3配置类
 *
 * @author 1907263405@qq.com
 * @date 2021/3/24 23:58
 */
@Configuration
public class Swagger3Config {

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.OAS_30)
                .apiInfo(apiInfo())
                .groupName("v1.0")
                .select()
                //这里指定Controller扫描包路径
                .apis(RequestHandlerSelectors.basePackage("work.pcdd.blogapi.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("博客API接口文档")
                .description("提供了构建博客网站的基本 RSETful API")
//                .termsOfServiceUrl("https://pcdd.work:8085")
                .contact(new Contact("Du Pengcheng", "https://blog.csdn.net/weixin_43553153", "1907263405@qq.com"))
                .version("1.0")
                .build();
    }

}
