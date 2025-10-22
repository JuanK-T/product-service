package com.linktic.challenge.products.domain.exception.valueobject;

public class InvalidRatingException extends ProductValueObjectException {
    public InvalidRatingException(String detailedMessage) {
        super("Invalid rating: " + detailedMessage);
    }
}