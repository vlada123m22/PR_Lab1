package com.pr.parser.controler;

import com.pr.parser.model.FilteredProductsResult;
import com.pr.parser.model.Product;
import com.pr.parser.service.ScrappingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ScrappingController {
    private final ScrappingService scrappingService;


    @GetMapping("/parse")
    public void handleRequest() {
        scrappingService.scrapPage();
    }
    @GetMapping("/")
    public Mono<List<Product>> getAllProducts() {
        return scrappingService.getAllProducts();
    }
    @GetMapping("/filter")
    public Mono<FilteredProductsResult> getFilteredProducts(@RequestParam String search) {
        return scrappingService.getFilteredProducts(search);
    }
}
