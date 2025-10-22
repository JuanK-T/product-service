package com.linktic.challenge.products.domain.model;

import com.linktic.challenge.products.domain.exception.valueobject.InvalidCategoryException;

public record ProductCategory(String value) {
    public ProductCategory {
        if (value != null && value.isBlank()) {
            throw new InvalidCategoryException("Category cannot be blank if provided");
        }
    }
}
