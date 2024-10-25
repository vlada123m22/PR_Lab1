package com.pr.parser.enumeration;

import java.math.BigDecimal;

public enum Currency {
    USD("USD", BigDecimal.valueOf(17.50), BigDecimal.valueOf(0.057)),
    EUR("EUR", BigDecimal.valueOf(20.00), BigDecimal.valueOf(0.050)),
    MDL("MDL", BigDecimal.ONE, BigDecimal.ONE);
    private final String currencyCode;
    private final BigDecimal toMdlRate;
    private final BigDecimal fromMdlRate;

    public String getCurrencyCode() {
        return currencyCode;
    }

    public BigDecimal getToMdlRate() {
        return toMdlRate;
    }

    public BigDecimal getFromMdlRate() {
        return fromMdlRate;
    }

    Currency(String currencyCode, BigDecimal toMdlRate, BigDecimal fromMdlRate) {
        this.currencyCode = currencyCode;
        this.toMdlRate = toMdlRate;
        this.fromMdlRate = fromMdlRate;
    }
}