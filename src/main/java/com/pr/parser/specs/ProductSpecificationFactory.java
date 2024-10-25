package com.pr.parser.specs;

import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class ProductSpecificationFactory {

    public ProductSpecification createSpecification(String key, String value) {
        if (key.equals("price")) {
            return new PriceComparisonSpecification(value);
        }
        return product -> true;
    }

    public ProductSpecification createCombinedSpecificationFromSearch(String search) {
        return Arrays.stream(search.split(","))
                .filter(condition -> condition != null && !condition.trim().isEmpty())
                .map(condition -> {
                    String[] parts = condition.split(":", 2);
                    if (parts.length < 2) {
                        return createSpecification("", "");
                    }
                    String key = parts[0].trim();
                    String filter = parts[1].trim();
                    return createSpecification(key, filter);
                })
                .reduce(product -> true, ProductSpecification::and);
    }


}
