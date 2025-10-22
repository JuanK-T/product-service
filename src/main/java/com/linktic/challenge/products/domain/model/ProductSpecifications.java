package com.linktic.challenge.products.domain.model;

import com.linktic.challenge.products.domain.exception.valueobject.InvalidSpecificationsException;

import java.util.Map;
import java.util.stream.Collectors;

public record ProductSpecifications(Map<String, String> specs) {

    public ProductSpecifications {
        if (specs == null) {
            specs = Map.of();
        }

        specs = specs.entrySet().stream()
                .filter(entry -> {
                    if (entry.getKey() == null || entry.getValue() == null) {
                        throw new InvalidSpecificationsException(
                                "Specification key or value cannot be null");
                    }

                    String key = entry.getKey().trim();
                    String value = entry.getValue().trim();

                    if (key.isEmpty() || value.isEmpty()) {
                        throw new InvalidSpecificationsException(
                                "Specification key or value cannot be empty");
                    }

                    if (key.length() > 50) {
                        throw new InvalidSpecificationsException(
                                "Specification key too long (max 50): " + key);
                    }

                    if (value.length() > 80) {
                        throw new InvalidSpecificationsException(
                                "Specification value too long (max 80) for key: " + key);
                    }

                    return true;
                })
                .collect(Collectors.toUnmodifiableMap(
                        entry -> entry.getKey().trim(),
                        entry -> entry.getValue().trim()
                ));

        if (specs.size() > 10) {
            throw new InvalidSpecificationsException("Maximum 10 specifications exceeded");
        }
    }
}