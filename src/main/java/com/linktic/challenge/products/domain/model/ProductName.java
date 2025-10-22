package com.linktic.challenge.products.domain.model;

import com.linktic.challenge.products.domain.exception.valueobject.InvalidProductNameException;

public record ProductName(String value) {
    public ProductName {
        if (value == null) {
            throw new InvalidProductNameException("Product name");
        }

        if (value.isBlank()) {
            throw new InvalidProductNameException("Product name cannot be blank");
        }
    }
}
