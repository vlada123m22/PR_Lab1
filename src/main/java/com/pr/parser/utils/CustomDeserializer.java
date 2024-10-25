package com.pr.parser.utils;

import com.pr.parser.enums.Currency;
import com.pr.parser.model.FilteredProductsResult;
import com.pr.parser.model.Product;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomDeserializer {

    public static FilteredProductsResult deserialize(String data) throws Exception {
        ParsePosition pos = new ParsePosition(0);

        expectPrefix("FilteredProductsResult:{", data, pos);
        return deserializeFilteredProductsResult(data, pos);
    }

    private static FilteredProductsResult deserializeFilteredProductsResult(String data, ParsePosition pos) throws Exception {
        FilteredProductsResult result = new FilteredProductsResult();

        System.out.println("Attempting to parse 'filteredProducts' at index: " + pos.currentIndex);
        expectKey("filteredProducts:", data, pos);
        result.setFilteredProducts(deserializeList(data, pos));

        System.out.println("Attempting to parse 'totalSum' at index: " + pos.currentIndex);
        expectKey("totalSum:", data, pos);
        result.setTotalSum(deserializeBigDecimal(data, pos));

        System.out.println("Attempting to parse 'timestamp' at index: " + pos.currentIndex);
        expectKey("timestamp:", data, pos);
        result.setTimestamp(deserializeInstant(data, pos));

        expectPrefix("}", data, pos);

        return result;
    }

    private static List<Product> deserializeList(String data, ParsePosition pos) {
        System.out.println("Deserializing list at index: " + pos.currentIndex);
        expectPrefix("L:", data, pos);
        int size = Integer.parseInt(nextToken(data, pos));
        List<Product> list = new ArrayList<>();

        expectPrefix("{", data, pos);
        for (int i = 0; i < size; i++) {
            list.add(deserializeProduct(data, pos));
        }
        expectPrefix("};", data, pos);

        return list;
    }

    private static Product deserializeProduct(String data, ParsePosition pos) {
        Product product = new Product();

        System.out.println("Deserializing product at index: " + pos.currentIndex);
        expectPrefix("Product:{", data, pos);

        expectKey("name:", data, pos);
        product.setName(deserializeString(data, pos));

        expectKey("price:", data, pos);
        product.setPrice(deserializeString(data, pos));

        expectKey("link:", data, pos);
        product.setLink(deserializeString(data, pos));

        expectKey("currency:", data, pos);

        String currencyStr = deserializeString(data, pos).replace("\"", "");
        product.setCurrency(Currency.valueOf(currencyStr));

        expectKey("characteristics:", data, pos);
        product.setCharacteristics(deserializeMap(data, pos));

        expectPrefix("}", data, pos);
        return product;
    }

    private static String deserializeString(String data, ParsePosition pos) {
        expectPrefix("S:", data, pos);
        int length = Integer.parseInt(nextToken(data, pos));
        System.out.println("Deserializing string of length: " + length);

        int stringStartIndex = pos.currentIndex;

        int stringEndIndex = data.indexOf("\";", stringStartIndex);

        if (stringEndIndex == -1) {
            throw new IllegalArgumentException("Invalid string serialization format. Could not find closing \";\"");
        }

        String value = data.substring(stringStartIndex, stringEndIndex);
        pos.currentIndex = stringEndIndex + 2;

        System.out.println("Deserialized string: " + value);
        return value;
    }


    private static BigDecimal deserializeBigDecimal(String data, ParsePosition pos) {
        expectPrefix("BD:", data, pos);

        int startIndex = pos.currentIndex;
        int endIndex = data.indexOf(';', startIndex);

        if (endIndex == -1) {
            throw new IllegalArgumentException("Invalid BigDecimal format. Missing ';' at the end.");
        }

        String value = data.substring(startIndex, endIndex);
        pos.currentIndex = endIndex + 1;

        System.out.println("Deserialized BigDecimal: " + value);
        return new BigDecimal(value);
    }


    private static Instant deserializeInstant(String data, ParsePosition pos) {
        expectPrefix("I:", data, pos);

        int startIndex = pos.currentIndex;
        int endIndex = data.indexOf(';', startIndex);

        if (endIndex == -1) {
            throw new IllegalArgumentException("Invalid Instant format. Missing ';' at the end.");
        }

        String value = data.substring(startIndex, endIndex);
        pos.currentIndex = endIndex + 1;

        System.out.println("Deserialized Instant: " + value);
        return Instant.parse(value);
    }


    private static Map<String, String> deserializeMap(String data, ParsePosition pos) {
        expectPrefix("M:", data, pos);
        int size = Integer.parseInt(nextToken(data, pos));
        Map<String, String> map = new HashMap<>();

        expectPrefix("{", data, pos);
        for (int i = 0; i < size; i++) {
            String key = deserializeString(data, pos);
            String value = deserializeString(data, pos);
            map.put(key, value);
        }
        expectPrefix("};", data, pos);

        System.out.println("Deserialized map: " + map);
        return map;
    }

    private static void expectKey(String key, String data, ParsePosition pos) {
        if (!data.startsWith(key, pos.currentIndex)) {
            throw new IllegalArgumentException("Expected key: " + key + " but got: " + data.substring(pos.currentIndex, Math.min(pos.currentIndex + 20, data.length())));
        }
        pos.currentIndex += key.length();
    }

    private static void expectPrefix(String prefix, String data, ParsePosition pos) {
        System.out.println("Expecting prefix: " + prefix + " at index: " + pos.currentIndex);
        if (!data.startsWith(prefix, pos.currentIndex)) {
            throw new IllegalArgumentException("Expected prefix: " + prefix + " but got: "
                    + data.substring(pos.currentIndex, Math.min(pos.currentIndex + 20, data.length())));
        }
        pos.currentIndex += prefix.length();
    }


    private static String nextToken(String data, ParsePosition pos) {
        int separatorIndex = data.indexOf(':', pos.currentIndex);
        if (separatorIndex == -1) {
            throw new IllegalArgumentException("Unexpected end of data at index: " + pos.currentIndex);
        }
        String token = data.substring(pos.currentIndex, separatorIndex);
        pos.currentIndex = separatorIndex + 1;
        return token;
    }

    private static class ParsePosition {

        int currentIndex;

        ParsePosition(int currentIndex) {
            this.currentIndex = currentIndex;
        }
    }
}
