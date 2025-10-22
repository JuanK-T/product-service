package com.linktic.challenge.products.domain.model;

import com.linktic.challenge.products.domain.exception.valueobject.InvalidBrandException;

public record ProductBrand(String value) {
    public ProductBrand {
        if (value != null && value.isBlank()) {
            throw new InvalidBrandException("Brand cannot be blank if provided");
        }
    }
}
