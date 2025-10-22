package com.linktic.challenge.products.domain.exception.valueobject;

public class InvalidBrandException extends ProductValueObjectException {
    public InvalidBrandException(String brand) {
        super("Invalid brand: " + brand);
    }
}