package com.linktic.challenge.products.domain.exception.valueobject;

public class InvalidProductIdException extends ProductValueObjectException {
    public InvalidProductIdException(String message) {
        super(message);
    }
}