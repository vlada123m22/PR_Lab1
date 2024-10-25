package com.pr.parser.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FilteredProductsResult {
    private List<Product> filteredProducts;
    private BigDecimal totalSum;
    private Instant timestamp;

    public String toString() {
        return "FilteredProductsResult(filteredProducts=" + this.getFilteredProducts() + ", totalSum=" + this.getTotalSum() + ", timestamp=" + this.getTimestamp() + ")";
    }
}
