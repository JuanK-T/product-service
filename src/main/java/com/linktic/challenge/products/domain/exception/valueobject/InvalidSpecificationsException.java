package com.linktic.challenge.products.domain.exception.valueobject;

public class InvalidSpecificationsException extends ProductValueObjectException {
    public InvalidSpecificationsException(String message) {
        super("Invalid specifications: " + message);
    }
}