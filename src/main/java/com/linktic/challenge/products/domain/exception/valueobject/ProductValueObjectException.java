package com.linktic.challenge.products.domain.exception.valueobject;

import com.linktic.challenge.products.domain.exception.ProductDomainException;

public abstract class ProductValueObjectException extends ProductDomainException {
    protected ProductValueObjectException(String message) {
        super(message);
    }
}