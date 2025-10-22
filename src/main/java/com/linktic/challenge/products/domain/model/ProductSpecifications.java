package com.linktic.challenge.products.domain.model;

import java.util.Map;
import java.util.stream.Collectors;

public record ProductSpecifications(Map<String, String> specs) {

    public ProductSpecifications {
        // Crear un Map inmutable sin elementos null
        specs = (specs != null)
                ? specs.entrySet().stream()
                .filter(entry -> entry.getKey() != null && entry.getValue() != null)
                .collect(Collectors.toUnmodifiableMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue
                ))
                : Map.of(); // Si no hay especificaciones, asignar un Map vacío
    }
}