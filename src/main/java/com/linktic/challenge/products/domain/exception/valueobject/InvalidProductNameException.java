package com.linktic.challenge.products.domain.exception.valueobject;

public class InvalidProductNameException extends ProductValueObjectException {
    public InvalidProductNameException(String name) {
        super("Invalid product name: " + name);
    }
}