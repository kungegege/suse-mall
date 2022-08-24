package com.frame.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Value("${swagger.enable}")
    private boolean enable;

    @Bean
    public Docket createDocket(){
        /*头部信息：header*/
        List<Parameter> par=new ArrayList<>();
        ParameterBuilder accessTokenBuilder=new ParameterBuilder();
        accessTokenBuilder.name("user-token").description("用户的AccessToken header入口")
                .modelRef(new ModelRef("String")).parameterType("header").required(false);
        par.add(accessTokenBuilder.build());


        /*配置信息*/
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.frame"))
                .paths(PathSelectors.any())
                .build()
                .globalOperationParameters(par)
                .enable(enable);
    }

    private ApiInfo apiInfo(){
        return new ApiInfoBuilder().title("")
                .description("接口文档")
                .termsOfServiceUrl("")
                .version("1.0")
                .build();
    }
}
