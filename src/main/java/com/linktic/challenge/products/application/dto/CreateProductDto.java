package com.linktic.challenge.products.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO para crear un producto")
public class CreateProductDto {

    @Schema(description = "Nombre del producto", example = "Smartphone Galaxy XZ")
    @NotBlank
    private String name;

    @Schema(description = "URL de la imagen del producto", example = "https://example.com/images/smartphone-xz.jpg")
    @NotBlank
    @Pattern(regexp = "^https?://.*")
    private String imageUrl;

    @Schema(description = "Descripción detallada del producto", example = "Teléfono inteligente de gama alta con pantalla AMOLED")
    private String description;

    @Schema(description = "Precio del producto", example = "899.99")
    @DecimalMin("0.0")
    private BigDecimal price;

    @Schema(description = "Moneda del precio", example = "USD", allowableValues = {"USD", "EUR", "COP"})
    @NotBlank
    private String currency;

    @Schema(description = "Rating del producto (0.0 - 5.0)", example = "4.7")
    @DecimalMin("0.0")
    @DecimalMax("5.0")
    private Double rating;

    @Schema(description = "Categoría del producto", example = "Electrónica")
    private String category;

    @Schema(description = "Marca del producto", example = "TechNova")
    private String brand;

    @Schema(description = "Especificaciones técnicas del producto")
    private Map<String, String> specifications;
}