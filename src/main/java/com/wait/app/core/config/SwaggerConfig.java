package com.wait.app.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
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

    private ApiInfo webApiInfo() {
        return new ApiInfoBuilder()
                .title("兜兜校园外卖API文档")
                .description("本文档描述了兜兜校园外卖的接口")
                .version("1.0")
                .build();
    }

    @Bean
    public Docket webApiConfig() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("小程序接口")
                .apiInfo(webApiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.wait.takeaway.controller"))
                .paths(PathSelectors.any())
                .build();
    }
}
