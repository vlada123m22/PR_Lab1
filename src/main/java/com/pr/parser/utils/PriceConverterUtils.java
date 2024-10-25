package com.pr.parser.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import com.pr.parser.enumeration.Currency;

public class PriceConverterUtils {
    private PriceConverterUtils() {
    }
    public static BigDecimal convertPrice(BigDecimal price, Currency from, Currency to) {
        if (from == to) {
            return price;
        }
        BigDecimal priceInMdl = price.multiply(from.getToMdlRate());
        return priceInMdl.multiply(to.getFromMdlRate()).setScale(2, RoundingMode.HALF_UP);
    }
}
