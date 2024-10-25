package com.pr.parser.specs;

import com.pr.parser.enums.ComparisonOperator;
import com.pr.parser.model.Product;

import java.math.BigDecimal;


public class PriceComparisonSpecification implements ProductSpecification {

    private final BigDecimal priceValue;

    private final ComparisonOperator operator;

    public PriceComparisonSpecification(String priceFilter) {
        this.operator = parseOperator(priceFilter);
        this.priceValue = parseValue(priceFilter);
    }

    private ComparisonOperator parseOperator(String priceFilter) {
        String operatorSymbol = priceFilter.replaceAll("[\\d]", "").trim();
        return ComparisonOperator.fromString(operatorSymbol);
    }

    private BigDecimal parseValue(String priceFilter) {
        return new BigDecimal(priceFilter.replaceAll("[^\\d]", ""));
    }

    @Override
    public boolean isSatisfiedBy(Product product) {
        BigDecimal productPrice = new BigDecimal(product.getPrice());

        return switch (operator) {
            case GREATER_THAN -> productPrice.compareTo(priceValue) > 0;
            case LESS_THAN -> productPrice.compareTo(priceValue) < 0;
            case EQUAL -> productPrice.compareTo(priceValue) == 0;
            case NOT_EQUAL -> productPrice.compareTo(priceValue) != 0;
            case GREATER_THAN_OR_EQUAL -> productPrice.compareTo(priceValue) >= 0;
            case LESS_THAN_OR_EQUAL -> productPrice.compareTo(priceValue) <= 0;
        };
    }
}


