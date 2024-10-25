package com.pr.parser.validation.rules;

import org.springframework.stereotype.Component;

@Component
public class PriceRule implements ValidatorRule<String> {

    @Override
    public String validate(String value) {
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException("Price cannot be null or empty");
        }

        var numericValue = value.replaceAll("[^\\d]", "");
        if (numericValue.isEmpty()) {
            throw new IllegalArgumentException("Price must contain at least one digit");
        }

        return numericValue;
    }
}
