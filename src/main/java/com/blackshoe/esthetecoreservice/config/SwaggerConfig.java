package com.blackshoe.esthetecoreservice.config;

import springfox.documentation.service.Server;
import springfox.documentation.spi.DocumentationType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import java.util.Collections;

@Configuration
public class SwaggerConfig {
    private static final String API_NAME = "Esthete Core Service API 명세서";
    private static final String API_VERSION = "1.0.0";
    private static final String API_DESCRIPTION = "Esthete";
    @Profile({"test || dev"})
    @Bean
    public Docket api() {
        Server serverLocal = new Server("local", "http://localhost:8080", "for local usages", Collections.emptyList(), Collections.emptyList());
        Server testingServer = new Server("test", "http://34.22.84.195", "for testing", Collections.emptyList(), Collections.emptyList());
        Server httpProductionServer = new Server("production", "http://api.esthete.roberniro-projects.xyz", "for production", Collections.emptyList(), Collections.emptyList());
        Server productionServer = new Server("production", "https://api.esthete.roberniro-projects.xyz", "for production", Collections.emptyList(), Collections.emptyList());

        return new Docket(DocumentationType.OAS_30)
                .servers(serverLocal, testingServer, httpProductionServer, productionServer)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
    }

    public ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(API_NAME)
                .version(API_VERSION)
                .description(API_DESCRIPTION)
                .build();
    }

    @Bean
    @Profile({"!test && !dev"})
    public Docket disable() {
        return new Docket(DocumentationType.OAS_30).enable(false);
    }
}
