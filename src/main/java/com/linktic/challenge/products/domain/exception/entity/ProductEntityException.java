package com.linktic.challenge.products.domain.exception.entity;

import com.linktic.challenge.products.domain.exception.ProductDomainException;

public abstract class ProductEntityException extends ProductDomainException {
    protected ProductEntityException(String message) {
        super(message);
    }

    protected ProductEntityException(String message, Throwable cause) {
        super(message, cause);
    }
}