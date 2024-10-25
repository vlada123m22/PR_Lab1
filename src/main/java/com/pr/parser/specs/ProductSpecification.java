package com.pr.parser.specs;

import com.pr.parser.model.Product;

@FunctionalInterface
public interface ProductSpecification {

    boolean isSatisfiedBy(Product product);

    default ProductSpecification and(ProductSpecification other) {
        return product -> this.isSatisfiedBy(product) && other.isSatisfiedBy(product);
    }

    default ProductSpecification or(ProductSpecification other) {
        return product -> this.isSatisfiedBy(product) || other.isSatisfiedBy(product);
    }
}

