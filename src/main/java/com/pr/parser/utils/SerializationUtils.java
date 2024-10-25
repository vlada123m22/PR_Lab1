package com.pr.parser.utils;

import com.pr.parser.model.FilteredProductsResult;
import com.pr.parser.model.Product;

public class SerializationUtils {

    private SerializationUtils() {
    }

    public static String serializeProductToJson(Product product) {
        StringBuilder json = new StringBuilder();
        json.append("{\n");
        json.append("  \"name\": \"").append(escapeJson(product.getName())).append("\",\n");
        json.append("  \"price\": \"").append(product.getPrice()).append("\",\n");
        json.append("  \"link\": \"").append(escapeJson(product.getLink())).append("\",\n");
        json.append("  \"currency\": \"").append(product.getCurrency().name()).append("\",\n");

        json.append("  \"characteristics\": {\n");
        if (product.getCharacteristics() != null && !product.getCharacteristics().isEmpty()) {
            product.getCharacteristics().forEach((key, value) -> {
                json.append("    \"").append(escapeJson(key)).append("\": \"").append(escapeJson(value)).append("\",\n");
            });
            json.setLength(json.length() - 2);
            json.append("\n");
        }
        json.append("  }\n");
        json.append("}");
        return json.toString();
    }

    public static String serializeProductToXml(Product product) {
        StringBuilder xml = new StringBuilder();
        xml.append("<product>\n");
        xml.append("  <name>").append(escapeXml(product.getName())).append("</name>\n");
        xml.append("  <price>").append(product.getPrice()).append("</price>\n");
        xml.append("  <link>").append(escapeXml(product.getLink())).append("</link>\n");
        xml.append("  <currency>").append(product.getCurrency().name()).append("</currency>\n");

        xml.append("  <characteristics>\n");
        if (product.getCharacteristics() != null && !product.getCharacteristics().isEmpty()) {
            product.getCharacteristics().forEach((key, value) -> {
                xml.append("    <characteristic name=\"").append(escapeXml(key)).append("\">")
                        .append(escapeXml(value)).append("</characteristic>\n");
            });
        }
        xml.append("  </characteristics>\n");
        xml.append("</product>");
        return xml.toString();
    }

    public static String serializeFilteredProductsResultToJson(FilteredProductsResult result) {
        StringBuilder json = new StringBuilder();
        json.append("{\n");
        json.append("  \"filteredProducts\": [\n");

        for (Product product : result.getFilteredProducts()) {
            json.append(serializeProductToJson(product)).append(",\n");
        }

        if (!result.getFilteredProducts().isEmpty()) {
            json.setLength(json.length() - 2);
            json.append("\n");
        }

        json.append("  ],\n");
        json.append("  \"totalSum\": \"").append(result.getTotalSum().toString()).append("\",\n");
        json.append("  \"timestamp\": \"").append(result.getTimestamp().toString()).append("\"\n");
        json.append("}");

        return json.toString();
    }

    public static String serializeFilteredProductsResultToXml(FilteredProductsResult result) {
        StringBuilder xml = new StringBuilder();
        xml.append("<filteredProductsResult>\n");

        xml.append("  <filteredProducts>\n");
        for (Product product : result.getFilteredProducts()) {
            xml.append(serializeProductToXml(product)).append("\n");
        }
        xml.append("  </filteredProducts>\n");

        xml.append("  <totalSum>").append(result.getTotalSum().toString()).append("</totalSum>\n");
        xml.append("  <timestamp>").append(result.getTimestamp().toString()).append("</timestamp>\n");

        xml.append("</filteredProductsResult>");
        return xml.toString();
    }

    private static String escapeJson(String input) {
        if (input == null) {
            return "";
        }
        return input.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\b", "\\b")
                .replace("\f", "\\f")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    private static String escapeXml(String input) {
        if (input == null) {
            return "";
        }
        return input.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&apos;");
    }

}
