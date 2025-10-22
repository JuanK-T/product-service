package com.linktic.challenge.products.domain.model;

import com.linktic.challenge.products.domain.exception.valueobject.InvalidImageUrlException;

import java.net.URI;

public record ProductImageUrl(String value) {
    private static final String DEFAULT_IMAGE = "https://st5.depositphotos.com/90358332/74974/v/450/depositphotos_749740000-stock-illustration-photo-thumbnail-graphic-element-found.jpg";

    public ProductImageUrl {
        if (value != null && value.isBlank()) {
            value = DEFAULT_IMAGE;
        }

        if (value != null && !isValidUrl(value)) {
            throw new InvalidImageUrlException(value);
        }
    }

    private boolean isValidUrl(String url) {
        try {
            URI uri = new URI(url);
            return "https".equals(uri.getScheme()) &&
                    uri.getHost() != null &&
                    !uri.getHost().trim().isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    public static ProductImageUrl of(String url) {
        return new ProductImageUrl(url);
    }
}