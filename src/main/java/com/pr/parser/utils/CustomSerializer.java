package com.pr.parser.utils;

import com.pr.parser.model.FilteredProductsResult;
import com.pr.parser.model.Product;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Collection;
import java.util.Map;

public class CustomSerializer {

    public static String serialize(FilteredProductsResult obj) throws IllegalAccessException {
        StringBuilder serialized = new StringBuilder();
        serialized.append("FilteredProductsResult:{");
        serialized.append("filteredProducts:");
        serialized.append(serializeList(obj.getFilteredProducts()));
        serialized.append("totalSum:");
        serialized.append(serializeBigDecimal(obj.getTotalSum()));
        serialized.append("timestamp:");
        serialized.append(serializeInstant(obj.getTimestamp()));
        serialized.append("}");
        return serialized.toString();
    }

    private static String serializeProduct(Product product) {
        StringBuilder serialized = new StringBuilder();
        serialized.append("Product:{");

        serialized.append("name:");
        serialized.append(serializeString(product.getName()));

        serialized.append("price:");
        serialized.append(serializeString(product.getPrice()));

        serialized.append("link:");
        serialized.append(serializeString(product.getLink()));

        serialized.append("currency:");
        serialized.append(serializeString(String.valueOf(product.getCurrency())));

        serialized.append("characteristics:");
        serialized.append(serializeMap(product.getCharacteristics()));

        serialized.append("}");
        return serialized.toString();
    }

    private static String serializeString(String value) {
        return "S:" + value.length() + ":\"" + value + "\";";
    }

    private static String serializeBigDecimal(BigDecimal value) {
        return "BD:" + value.toString() + ";";
    }

    private static String serializeInstant(Instant value) {
        return "I:" + value.toString() + ";";
    }

    private static String serializeList(Collection<Product> list) throws IllegalAccessException {
        StringBuilder serialized = new StringBuilder();
        serialized.append("L:").append(list.size()).append(":{");

        for (Product item : list) {
            serialized.append(serializeProduct(item));
        }

        serialized.append("};");
        return serialized.toString();
    }

    private static String serializeMap(Map<String, String> map) {
        StringBuilder serialized = new StringBuilder();
        serialized.append("M:").append(map.size()).append(":{");

        for (Map.Entry<String, String> entry : map.entrySet()) {
            serialized.append(serializeString(entry.getKey()));
            serialized.append(serializeString(entry.getValue()));
        }

        serialized.append("};");
        return serialized.toString();
    }
}
