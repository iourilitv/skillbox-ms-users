package com.example.microservices.users.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenApi() {
        return new OpenAPI().components(new Components())
                .info(new Info()
                        .title("Rest API of Users microservice")
                        .description("Rest API of Users microservice as a demo project on Spring Boot")
                        .version("0.0.1"));
    }
}
