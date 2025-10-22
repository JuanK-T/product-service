package com.linktic.challenge.products.domain.exception.entity;

public class InvalidProductException extends ProductEntityException {
    public InvalidProductException(String message) {
        super(message);
    }
}