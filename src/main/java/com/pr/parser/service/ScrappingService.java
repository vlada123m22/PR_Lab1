package com.pr.parser.service;

import com.pr.parser.config.ScrappingProperties;
import com.pr.parser.validation.ProductValidator;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import com.pr.parser.model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class ScrappingService {
    private final WebClientService webClientService;
    private final ProductValidator productValidator;
    private final ScrappingProperties scrappingProperties;

    public List<Product> parseHtmlForProducts(String html) {
        List<Product> products = new ArrayList<>();
        Document document = Jsoup.parse(html);
        Elements productElements = document.select(".js-itemsList-item");
        for (Element productElement : productElements) {
            var productName = productElement.select("meta[itemprop=name]").attr("content");
            var productPrice = productElement.select(".card-price_curr").text();
            var productLink = scrappingProperties.getBaseUrl() + productElement.select("a[itemprop=url]").attr("href");

            var product = Product.builder()
                    .name(productName)
                    .price(productPrice)
                    .link(productLink)
                    .build();

            productValidator.validate(product);

            products.add(product);

        }
        return products;
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
    public void scrapPage() {
        webClientService.fetchHtmlContent("/ro/catalog/electronics/telephones/mobile/?page_=page_3").subscribe(htmlContent -> {
            List<Product> products = parseHtmlForProducts(htmlContent);
            for (Product product : products) {
                scrapeAdditionalData(product.getLink()).subscribe(characteristics -> {
                    product.setCharacteristics(characteristics);
                    System.out.println("Product name: " + product.getName());
                    System.out.println("Product price: " +product.getPrice());
                    System.out.println("Product Link: " + product.getLink());
                    System.out.println("Product details: " +product.getCharacteristics());
                });
            }
        });
    }
}