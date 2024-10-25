package com.pr.parser.enums;

import lombok.Getter;

@Getter
public enum ComparisonOperator {
    GREATER_THAN(">"),
    LESS_THAN("<"),
    EQUAL("="),
    NOT_EQUAL("!="),
    GREATER_THAN_OR_EQUAL(">="),
    LESS_THAN_OR_EQUAL("<=");

    private final String symbol;

    ComparisonOperator(String symbol) {
        this.symbol = symbol;
    }

    public static ComparisonOperator fromString(String symbol) {
        for (ComparisonOperator operator : ComparisonOperator.values()) {
            if (operator.symbol.equals(symbol)) {
                return operator;
            }
        }
        throw new IllegalArgumentException("Unsupported operator: " + symbol);
    }
}

