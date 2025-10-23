package com.linktic.challenge.products.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.math.BigDecimal;
import java.util.Map;

@Schema(description = "Representación de un producto en el sistema")
public record ProductDto(
        @Schema(description = "Identificador único del producto", example = "prod001")
        @NotBlank String id,

        @Schema(description = "Nombre del producto", example = "Smartphone Galaxy XZ")
        @NotBlank String name,

        @Schema(description = "URL de la imagen del producto", example = "https://example.com/images/smartphone-xz.jpg")
        @NotBlank @Pattern(regexp = "^https?://.*") String imageUrl,

        @Schema(description = "Descripción detallada del producto", example = "Teléfono inteligente de gama alta con pantalla AMOLED")
        String description,

        @Schema(description = "Precio del producto", example = "899.99")
        @DecimalMin("0.0") BigDecimal price,

        @Schema(description = "Moneda del precio", example = "USD", allowableValues = {"USD", "EUR", "COP"})
        @NotBlank String currency,

        @Schema(description = "Rating del producto (0.0 - 5.0)", example = "4.7")
        @DecimalMin("0.0") @DecimalMax("5.0") Double rating,

        @Schema(description = "Categoría del producto", example = "Electrónica")
        String category,

        @Schema(description = "Marca del producto", example = "TechNova")
        String brand,

        @Schema(description = "Especificaciones técnicas del producto")
        Map<String, String> specifications
) {}
