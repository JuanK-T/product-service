package com.linktic.challenge.products.domain.exception.valueobject;

public class InvalidImageUrlException extends ProductValueObjectException {
    public InvalidImageUrlException(String url) {
        super("Invalid image URL: " + url);
    }
}