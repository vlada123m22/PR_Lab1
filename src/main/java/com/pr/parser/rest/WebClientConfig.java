package com.pr.parser.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@RequiredArgsConstructor
public class WebClientConfig {

    private final ScrappingProperties scrappingProperties;

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl(scrappingProperties.getBaseUrl())
                .build();
    }
}
