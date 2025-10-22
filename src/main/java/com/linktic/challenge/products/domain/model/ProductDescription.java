package com.linktic.challenge.products.domain.model;

import com.linktic.challenge.products.domain.exception.valueobject.InvalidProductDescriptionException;

public record ProductDescription(String value) {
    private static final int MIN_LENGTH = 10;
    private static final int MAX_LENGTH = 500;

    public ProductDescription {
        if (value == null) {
            throw new InvalidProductDescriptionException("Product description cannot be null");
        }

        if (value.isBlank()) {
            throw new InvalidProductDescriptionException("Product description cannot be blank");
        }

        if (value.length() < MIN_LENGTH) {
            throw new InvalidProductDescriptionException(
                    String.format("Product description must be at least %d characters", MIN_LENGTH)
            );
        }

        if (value.length() > MAX_LENGTH) {
            throw new InvalidProductDescriptionException(
                    String.format("Product description cannot exceed %d characters", MAX_LENGTH)
            );
        }
    }
}