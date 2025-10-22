package com.linktic.challenge.products.domain.model;

import com.linktic.challenge.products.domain.exception.valueobject.InvalidCategoryException;

public record ProductCategory(String value) {
    private static final int MIN_LENGTH = 2;
    private static final int MAX_LENGTH = 50;
    private static final String VALID_PATTERN = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ0-9\\s\\-&.,()]+$";
    private static final String LETTER_PATTERN = ".*[a-zA-ZáéíóúÁÉÍÓÚñÑ].*";

    public ProductCategory {
        if (value != null) {
            if (value.isBlank()) {
                throw new InvalidCategoryException("Category cannot be blank if provided");
            }

            if (value.length() < MIN_LENGTH) {
                throw new InvalidCategoryException(
                        String.format("Category must be at least %d characters long", MIN_LENGTH)
                );
            }

            if (value.length() > MAX_LENGTH) {
                throw new InvalidCategoryException(
                        String.format("Category cannot exceed %d characters", MAX_LENGTH)
                );
            }

            if (!value.matches(VALID_PATTERN)) {
                throw new InvalidCategoryException("Category contains invalid characters");
            }

            if (!value.matches(LETTER_PATTERN)) {
                throw new InvalidCategoryException("Category must contain at least one letter");
            }
        }
    }

    public static ProductCategory of(String value) {
        return new ProductCategory(value);
    }
}
