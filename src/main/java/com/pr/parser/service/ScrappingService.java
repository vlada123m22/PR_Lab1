package com.pr.parser.service;

import com.pr.parser.config.ScrappingProperties;
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

@Service
@RequiredArgsConstructor
public class ScrappingService {
    private final WebClientService webClientService;

    private final ScrappingProperties scrappingProperties;

    public List<Product> parseHtmlForProducts(String html) {
        List<Product> products = new ArrayList<>();
        Document document = Jsoup.parse(html);
        Elements productElements = document.select(".js-itemsList-item");
        for (Element productElement : productElements) {
            var productName = productElement.select("meta[itemprop=name]").attr("content");
            var productPrice = productElement.select(".card-price_curr").text();
            var productLink = scrappingProperties.getBaseUrl() + productElement.select("a[itemprop=url]").attr("href");
            var product = new Product();
            product.setName(productName);
            product.setPrice(productPrice);
            product.setLink(productLink);
            products.add(product);
        }
        return products;
    }


    public String scrapPage() {
        Mono<String> result = webClientService.fetchHtmlContent("/catalog/electronics/telephones/mobile/?page_=page_3");
        result.subscribe(htmlContent -> {
            List<Product> products = parseHtmlForProducts(htmlContent);
            //products.forEach(System.out::println);
            for (Product p : products) {
                System.out.println( "Product Name: "  + p.getName());
                System.out.println("Product Price: "+ p.getPrice());
                System.out.println("Product Link: " + p.getLink());
            }
            System.out.println("Total products: " + products.size());
        });
        return result.block();
    }
}