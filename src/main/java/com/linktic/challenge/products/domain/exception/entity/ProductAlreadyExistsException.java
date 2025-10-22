package com.linktic.challenge.products.domain.exception.entity;

import lombok.Getter;

@Getter
public class ProductAlreadyExistsException extends ProductEntityException {
    private final String productName;

    public ProductAlreadyExistsException(String productName) {
        super("Product already exists with name: " + productName);
        this.productName = productName;
    }
}