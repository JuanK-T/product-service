package com.linktic.challenge.products.domain.exception;

public abstract class ProductDomainException extends RuntimeException {
    protected ProductDomainException(String message) {
        super(message);
    }

    protected ProductDomainException(String message, Throwable cause) {
        super(message, cause);
    }
}

