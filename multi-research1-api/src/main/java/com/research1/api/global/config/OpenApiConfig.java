package com.research1.api.global.config;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;


@Configuration
@RequiredArgsConstructor
@OpenAPIDefinition(
        info = @Info(title = "INFLINKER JPA API",
                description = "INFLINKER API 명세",
                version = "v1"),
        servers = {
                @Server(url = "/", description = "Default Server URL"),
        })
@SecurityScheme(
        name = "Authorization",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "Bearer"
)

public class OpenApiConfig {

    private static final String REFERENCE = "Authorization";


    @Bean
    public Docket api() {
        return new Docket(DocumentationType.OAS_30)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.inflinker.jpaapi.controller"))
                .paths(PathSelectors.ant("/api/**"))
                .build().apiInfo(apiInfo())
                .securityContexts(List.of(securityContext()))
                .securitySchemes(List.of(apiKey()));
    }

    // PROJECT Description
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("INFLINKER JPA v1")
                .description("INFLINKER JPA VERSION1")
                .version("1.0.0")
                .build();
    }

    // This class provides a repository for Authentication objects, allowing them to be retrieved and used whenever needed.
// It's designed to be stored in ThreadLocal, making it accessible from anywhere.
// Once authentication is complete, it's stored in HttpSession, making it globally accessible throughout the application.
    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .build();
    }

    // Set Swagger Authorization permissions
    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return List.of(new SecurityReference("Authorization", authorizationScopes));
    }

    private ApiKey apiKey() {
        return new ApiKey(REFERENCE, "Bearer", "header");
    }


}
