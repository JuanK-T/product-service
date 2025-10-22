package com.linktic.challenge.products.domain.model;

import com.linktic.challenge.products.domain.exception.valueobject.InvalidProductIdException;

public record ProductId(String value) {
    public ProductId {
        if (value == null) {
            throw new InvalidProductIdException("Product id cannot be null");
        }

        if (value.isBlank()) {
            throw new InvalidProductIdException("Product id cannot be blank");
        }

        if (!value.matches("^[a-zA-Z0-9_-]+$")) {
            throw new InvalidProductIdException("Product id contains invalid characters");
        }
    }
}
