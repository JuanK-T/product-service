package com.linktic.challenge.products.domain.exception.mapper;

import com.linktic.challenge.products.domain.exception.ProductDomainException;

public class ProductMapperException extends ProductDomainException {
    public ProductMapperException(String message) {
        super(message);
    }

    public ProductMapperException(String message, Throwable cause) {
        super(message, cause);
    }
}