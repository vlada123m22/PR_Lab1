package com.pr.parser.validation.rules;

import org.springframework.stereotype.Component;

@Component
public class NameRule implements ValidatorRule<String> {

    @Override
    public String validate(String value) {
        if (value == null) {
            throw new IllegalArgumentException("Product name cannot be null");
        }
        return value.trim();
    }

}
