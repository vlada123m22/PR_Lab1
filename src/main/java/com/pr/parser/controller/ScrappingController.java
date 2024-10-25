package com.pr.parser.controller;

import com.pr.parser.model.FilteredProductsResult;
import com.pr.parser.model.Product;
import com.pr.parser.service.ScrappingService;
import com.pr.parser.utils.CustomDeserializer;
import com.pr.parser.utils.CustomSerializer;
import com.pr.parser.utils.PHPSerializer;
import com.pr.parser.utils.SerializationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ScrappingController {

    private final ScrappingService scrappingService;

    @GetMapping("/")
    public Mono<List<Product>> getAllProducts() {
        return scrappingService.getAllProducts();
    }

    @GetMapping("/filter")
    public Mono<FilteredProductsResult> getFilteredProducts(@RequestParam String search) {
        return scrappingService.getFilteredProducts(search);
    }

    @GetMapping(value = "/filter-custom")
    public ResponseEntity<String> getFilteredProductsCustomSerialization(@RequestParam String search,
                                                      @RequestParam(name = "format", required = false, defaultValue = "json") String format) throws Exception {
        var result = scrappingService.getFilteredProducts(search).block();

        var serializedData = CustomSerializer.serialize(result);
        System.out.println(serializedData);

        var deserializedResult = CustomDeserializer.deserialize(serializedData);
        System.out.println(deserializedResult);

        var t = PHPSerializer.serializeObject(result);
        System.out.println(t);

        if (result == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No products found.");
        }

        if ("xml".equalsIgnoreCase(format)) {
            var xmlResult = SerializationUtils.serializeFilteredProductsResultToXml(result);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_XML_VALUE)
                    .body(xmlResult);
        } else {
            var jsonResult = SerializationUtils.serializeFilteredProductsResultToJson(result);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .body(jsonResult);
        }
    }

}
