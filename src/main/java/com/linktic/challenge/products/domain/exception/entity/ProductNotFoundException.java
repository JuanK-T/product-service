package com.linktic.challenge.products.domain.exception.entity;

import lombok.Getter;

@Getter
public class ProductNotFoundException extends ProductEntityException {
    private final String productId;

    public ProductNotFoundException(String productId) {
        super("Product not found with ID: " + productId);
        this.productId = productId;
    }

}
