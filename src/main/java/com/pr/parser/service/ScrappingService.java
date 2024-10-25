package com.pr.parser.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ScrappingService {
    private final WebClientService webClientService;


    public String scrapPage() {
        Mono<String> result = webClientService.fetchHtmlContent("/catalog/electronics/telephones/mobile/?page_=page_3");
        result.subscribe(htmlContent -> {
            System.out.println("HTML Content from relative path: ");
            System.out.println(htmlContent);
        });
        return result.block();
    }
}