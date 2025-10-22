package com.linktic.challenge.products.domain.model;

import com.linktic.challenge.products.domain.exception.valueobject.InvalidRatingException;

public record ProductRating(Double value) {
    public ProductRating {
        if (value == null) {
            throw new InvalidRatingException("Cannot be null");
        }

        if (value < 0 || value > 5) {
            throw new InvalidRatingException("Must be between 0 and 5 (inclusive)");
        }

        if (value.isNaN() || value.isInfinite()) {
            throw new InvalidRatingException("Must be a finite number");
        }
    }

    public static ProductRating of(Double value) {
        return new ProductRating(value);
    }
}