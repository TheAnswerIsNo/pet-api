package com.wait.app.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;
/**
 * @author 天
 *
 * @description: swagger配置
 */
@Configuration
@EnableSwagger2WebMvc
public class SwaggerConfig {

    @Bean
    public Docket systemApiConfig() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("系统接口")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.wait.app.controller.system"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(new ApiInfoBuilder()
                        .contact(new Contact("秋","https://github.com/TheAnswerIsNo","2512448214@qq.com"))
                        .title("爱宠后台系统接口文档")
                        .description("本文档描述了爱宠小程序后台系统的接口")
                        .version("1.0")
                        .build());
    }

    @Bean
    public Docket webApiConfig() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("后台接口")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.wait.app.controller.web"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(new ApiInfoBuilder()
                        .contact(new Contact("秋","https://github.com/TheAnswerIsNo","2512448214@qq.com"))
                        .title("爱宠后台接口文档")
                        .description("本文档描述了爱宠后台的接口")
                        .version("1.0")
                        .build());
    }

    @Bean
    public Docket appletApiConfig() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("小程序接口")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.wait.app.controller.applet"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(new ApiInfoBuilder()
                        .contact(new Contact("秋","https://github.com/TheAnswerIsNo","2512448214@qq.com"))
                        .title("爱宠小程序接口文档")
                        .description("本文档描述了爱宠小程序的接口")
                        .version("1.0")
                        .build());
    }
}
