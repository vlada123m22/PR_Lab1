package com.pr.parser.service;

import com.pr.parser.enumeration.Currency;
import com.pr.parser.model.FilteredProductsResult;
import com.pr.parser.model.Product;
import com.pr.parser.config.ScrappingProperties;
import com.pr.parser.specs.ProductSpecificationFactory;
import com.pr.parser.utils.PriceConverterUtils;
import com.pr.parser.validation.ProductValidator;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ScrappingService {

    private final WebClientService webClientService;

    private final ScrappingProperties scrappingProperties;

    private final ProductValidator productValidator;

    private final ProductSpecificationFactory productSpecificationFactory;
    public Flux<Product> parseHtmlForProducts(String html) {
        Document document = Jsoup.parse(html);
        Elements productElements = document.select(".js-itemsList-item");

        return Flux.fromIterable(productElements)
                .flatMap(productElement -> {
                    var productName = productElement.select("meta[itemprop=name]").attr("content");
                    var productPrice = productElement.select(".card-price_curr").text();
                    var productLink = scrappingProperties.getBaseUrl() + productElement.select("a[itemprop=url]").attr("href");
                    var product = Product.builder()
                            .name(productName)
                            .price(productPrice)
                            .link(productLink)
                            .currency(Currency.MDL)
                            .build();
                    productValidator.validate(product);
                    return scrapeAdditionalData(product.getLink())
                            .map(characteristics -> {
                                product.setCharacteristics(characteristics);
                                return product;
                            });
                });
    }

    public Mono<Map<String, String>> scrapeAdditionalData(String productLink) {
        return webClientService.fetchHtmlContent(productLink)
                .map(html -> parseCharacteristics(Jsoup.parse(html)));
    }

    private Map<String, String> parseCharacteristics(Document document) {
        return document.select("div.tab-pane-inner")
                .select("table")
                .select("tr")
                .stream()
                .map(row -> row.select("td"))
                .filter(columns -> columns.size() == 2)
                .collect(
                        HashMap::new,
                        (map, columns) -> map.put(columns.get(0).text(), columns.get(1).text()),
                        HashMap::putAll
                );
    }

    public Mono<FilteredProductsResult> processProducts(List<Product> products, String params) {
        Instant timestamp = Instant.now();

        var priceSpec = productSpecificationFactory.createCombinedSpecificationFromSearch(params);
        return Flux.fromIterable(products)
                .filter(priceSpec::isSatisfiedBy)
                .flatMap(product -> {
                    var convertedPrice = PriceConverterUtils.convertPrice(new BigDecimal(product.getPrice()), Currency.MDL, Currency.EUR);
                    product.setPrice(convertedPrice.toString());
                    product.setCurrency(Currency.EUR);
                    return Mono.just(product);
                })
                .collectList()
                .flatMap(filteredProducts -> {
                    var totalSum = filteredProducts.stream()
                            .map(product -> new BigDecimal(product.getPrice()))
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    return Mono.just(new FilteredProductsResult(filteredProducts, totalSum, timestamp));
                });
    }
    public Mono<List<Product>> getAllProducts() {
        return webClientService.fetchHtmlContent("/ro/catalog/electronics/telephones/mobile/?page_=page_3")
                .flatMapMany(this::parseHtmlForProducts)
                .collectList();
    }
    public Mono<FilteredProductsResult> getFilteredProducts(String params) {
        return webClientService.fetchHtmlContent("/ro/catalog/electronics/telephones/mobile/?page_=page_3")
                .flatMapMany(this::parseHtmlForProducts)
                .collectList()
                .flatMap(products -> processProducts(products, params));
    }
    public void scrapPage() {
//        webClientService.fetchHtmlContent("/ru/catalog/electronics/telephones/mobile/?page_=page_3")
//                .flatMapMany(this::parseHtmlForProducts)
//                .collectList()
//                .flatMap(products -> processProducts(products, ">= 1000"))
//                .subscribe(result -> {
//                    System.out.println("Filtered Products: " + result.getFilteredProducts());
//                    System.out.println("Total Sum: " + result.getTotalSum());
//                    System.out.println("Timestamp: " + result.getTimestamp());
//                }, error -> {
//                    System.err.println("Error occurred: " + error.getMessage());
//                });
    }
}