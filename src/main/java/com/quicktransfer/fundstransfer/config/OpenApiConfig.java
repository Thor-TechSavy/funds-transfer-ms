package com.quicktransfer.fundstransfer.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    private final OpenApiProperties properties;

    public OpenApiConfig(OpenApiProperties properties) {
        this.properties = properties;
    }

    @Bean
    public OpenAPI customOpenAPI() {

        var server = new Server()
                .url(properties.getServerUrl())
                .description(properties.getEnvironment());

        var info = new Info()
                .title(properties.getTitle())
                .version(properties.getAppVersion())
                .description(properties.getDescription())
                .license(new License().name("Apache 2.0").url("http://techthorsavy.org"));

        return new OpenAPI()
                .addServersItem(server)
                .info(info);
    }

}
