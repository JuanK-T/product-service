package com.linktic.challenge.products.domain.model;

import com.linktic.challenge.products.domain.exception.valueobject.InvalidPriceException;

import java.math.BigDecimal;
import java.util.Currency;

public record ProductPrice(BigDecimal value, Currency currency) {

    public ProductPrice {
        if (value == null) {
            throw new InvalidPriceException("Price value cannot be null");
        }
        if (value.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidPriceException("Price must be non-negative");
        }
        if (currency == null) {
            throw new InvalidPriceException("Price currency cannot be null");
        }
    }

    public static ProductPrice of(BigDecimal value, Currency currency) {
        return new ProductPrice(value, currency);
    }

    public ProductPrice add(ProductPrice other) {
        assertSameCurrency(other);
        return new ProductPrice(this.value.add(other.value), this.currency);
    }

    private void assertSameCurrency(ProductPrice other) {
        if (!this.currency.equals(other.currency)) {
            throw new InvalidPriceException("Price cannot add prices with different currencies");
        }
    }
}