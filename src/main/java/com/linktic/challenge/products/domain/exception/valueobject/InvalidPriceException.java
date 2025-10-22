package com.linktic.challenge.products.domain.exception.valueobject;

public class InvalidPriceException extends ProductValueObjectException {
    public InvalidPriceException(String message) {
        super(message);
    }
}
