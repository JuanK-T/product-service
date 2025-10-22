package com.linktic.challenge.products.domain.exception.valueobject;

public class InvalidProductDescriptionException extends ProductValueObjectException{
    public InvalidProductDescriptionException(String message) {
        super(message);
    }
}
