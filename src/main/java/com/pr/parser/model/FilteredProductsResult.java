package com.pr.parser.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public class FilteredProductsResult {
    private List<Product> filteredProducts;
    private BigDecimal totalSum;
    private Instant timestamp;

    public FilteredProductsResult(List<Product> filteredProducts, BigDecimal totalSum, Instant timestamp) {
        this.filteredProducts = filteredProducts;
        this.totalSum = totalSum;
        this.timestamp = timestamp;
    }

    public List<Product> getFilteredProducts() {
        return filteredProducts;
    }

    public void setFilteredProducts(List<Product> filteredProducts) {
        this.filteredProducts = filteredProducts;
    }

    public BigDecimal getTotalSum() {
        return totalSum;
    }

    public void setTotalSum(BigDecimal totalSum) {
        this.totalSum = totalSum;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }
}