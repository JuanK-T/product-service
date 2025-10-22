package com.linktic.challenge.products.domain.exception.valueobject;

public class InvalidCategoryException extends ProductValueObjectException {
    public InvalidCategoryException(String category) {
        super("Invalid category: " + category);
    }
}