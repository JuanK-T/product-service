package com.linktic.challenge.products.domain.model;

import com.linktic.challenge.products.domain.exception.valueobject.InvalidProductNameException;

public record ProductName(String value) {
    private static final int MIN_LENGTH = 4;
    private static final int MAX_LENGTH = 60;

    public ProductName {
        if (value == null) {
            throw new InvalidProductNameException("Product name cannot be null");
        }

        if (value.isBlank()) {
            throw new InvalidProductNameException("Product name cannot be blank");
        }

        if (value.length() < MIN_LENGTH) {
            throw new InvalidProductNameException("Product name must be at least " + MIN_LENGTH + " character" + (MIN_LENGTH != 1 ? "s" : ""));
        }

        if (value.length() > MAX_LENGTH) {
            throw new InvalidProductNameException("Product name cannot exceed " + MAX_LENGTH + " characters");
        }
    }
}
