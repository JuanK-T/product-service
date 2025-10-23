package com.linktic.challenge.products.application.dto;

import java.math.BigDecimal;
import java.util.Map;

public record UpdateProductDto(
        String name,
        String imageUrl,
        String description,
        BigDecimal price,
        String currency,
        Double rating,
        String category,
        String brand,
        Map<String, String> specifications
) {}