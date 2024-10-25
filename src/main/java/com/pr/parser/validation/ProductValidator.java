package com.pr.parser.validation;

import com.pr.parser.model.Product;

import com.pr.parser.rules.NameRule;
import com.pr.parser.rules.PriceRule;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
@Component
@RequiredArgsConstructor
public class ProductValidator {
    private final NameRule nameValidator;
    private final PriceRule priceValidator;
    public void validate(Product product) {
        product.setName(nameValidator.validate(product.getName()));
        product.setPrice(priceValidator.validate(product.getPrice()));
    }
}