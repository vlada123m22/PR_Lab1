package com.pr.parser.model;

import com.pr.parser.enums.Currency;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    private String name;
    private String price;
    private String link;
    private Currency currency;
    private Map<String, String> characteristics;

    public String toString() {
        return "Product(name=" + this.getName() + ", price=" + this.getPrice() + ", link=" + this.getLink() + ", currency=" + this.getCurrency() + ", characteristics=" + this.getCharacteristics() + ")";
    }
}
