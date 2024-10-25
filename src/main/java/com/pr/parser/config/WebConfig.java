package com.pr.parser.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
@Configuration
public class WebConfig {
    private final ScrappingProperties scrappingProperties;

    public WebConfig(ScrappingProperties scrappingProperties) {
        this.scrappingProperties = scrappingProperties;
    }

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl(scrappingProperties.getBaseUrl())
                .build();
    }
}