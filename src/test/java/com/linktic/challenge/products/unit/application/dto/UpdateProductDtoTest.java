package com.linktic.challenge.products.unit.application.dto;

import com.linktic.challenge.products.application.dto.UpdateProductDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class UpdateProductDtoTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @Test
    @DisplayName("Dado un UpdateProductDto completamente nulo, cuando se valida, entonces no debe tener violaciones de validación")
    void givenCompletelyNullUpdateProductDto_whenValidated_thenShouldNotHaveValidationViolations() {
        // Given
        UpdateProductDto dto = new UpdateProductDto(
                null, null, null, null, null, null, null, null, null
        );

        // When
        Set<ConstraintViolation<UpdateProductDto>> violations = validator.validate(dto);

        // Then
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Dado un UpdateProductDto con algunos campos nulos, cuando se valida, entonces no debe tener violaciones de validación")
    void givenPartiallyNullUpdateProductDto_whenValidated_thenShouldNotHaveValidationViolations() {
        // Given
        UpdateProductDto dto = new UpdateProductDto(
                "Updated Name",
                null, // imageUrl nulo
                "Updated description",
                null, // price nulo
                null, // currency nulo
                4.8,  // rating proporcionado
                null, // category nulo
                "Updated Brand",
                null  // specifications nulo
        );

        // When
        Set<ConstraintViolation<UpdateProductDto>> violations = validator.validate(dto);

        // Then
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Dado un UpdateProductDto con todos los campos proporcionados, cuando se valida, entonces no debe tener violaciones de validación")
    void givenFullyPopulatedUpdateProductDto_whenValidated_thenShouldNotHaveValidationViolations() {
        // Given
        UpdateProductDto dto = new UpdateProductDto(
                "Updated Smartphone Galaxy XZ",
                "https://example.com/updated-image.jpg",
                "Updated smartphone description with new features",
                new BigDecimal("799.99"),
                "EUR",
                4.8,
                "Updated Electronics",
                "Updated TechNova",
                Map.of(
                        "screen", "6.7 inches Super AMOLED",
                        "processor", "Snapdragon 8 Gen 3",
                        "memory", "512GB"
                )
        );

        // When
        Set<ConstraintViolation<UpdateProductDto>> violations = validator.validate(dto);

        // Then
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Dado un UpdateProductDto con price negativo, cuando se valida, entonces no debe tener violaciones de validación")
    void givenUpdateProductDtoWithNegativePrice_whenValidated_thenShouldNotHaveValidationViolations() {
        // Given
        UpdateProductDto dto = new UpdateProductDto(
                "Product Name",
                "https://example.com/image.jpg",
                "Description",
                new BigDecimal("-100.00"), // Precio negativo permitido en updates
                "USD",
                4.5,
                "Electronics",
                "Brand",
                Map.of("key", "value")
        );

        // When
        Set<ConstraintViolation<UpdateProductDto>> violations = validator.validate(dto);

        // Then
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Dado un UpdateProductDto con rating fuera de rango, cuando se valida, entonces no debe tener violaciones de validación")
    void givenUpdateProductDtoWithOutOfRangeRating_whenValidated_thenShouldNotHaveValidationViolations() {
        // Given
        UpdateProductDto dto = new UpdateProductDto(
                "Product Name",
                "https://example.com/image.jpg",
                "Description",
                new BigDecimal("100.00"),
                "USD",
                10.0, // Rating fuera de rango permitido en updates
                "Electronics",
                "Brand",
                Map.of("key", "value")
        );

        // When
        Set<ConstraintViolation<UpdateProductDto>> violations = validator.validate(dto);

        // Then
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Dado un UpdateProductDto con imageUrl inválida, cuando se valida, entonces no debe tener violaciones de validación")
    void givenUpdateProductDtoWithInvalidImageUrl_whenValidated_thenShouldNotHaveValidationViolations() {
        // Given
        UpdateProductDto dto = new UpdateProductDto(
                "Product Name",
                "invalid-url", // URL inválida permitida en updates
                "Description",
                new BigDecimal("100.00"),
                "USD",
                4.5,
                "Electronics",
                "Brand",
                Map.of("key", "value")
        );

        // When
        Set<ConstraintViolation<UpdateProductDto>> violations = validator.validate(dto);

        // Then
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Dado un UpdateProductDto con currency vacía, cuando se valida, entonces no debe tener violaciones de validación")
    void givenUpdateProductDtoWithEmptyCurrency_whenValidated_thenShouldNotHaveValidationViolations() {
        // Given
        UpdateProductDto dto = new UpdateProductDto(
                "Product Name",
                "https://example.com/image.jpg",
                "Description",
                new BigDecimal("100.00"),
                "", // Currency vacía permitida en updates
                4.5,
                "Electronics",
                "Brand",
                Map.of("key", "value")
        );

        // When
        Set<ConstraintViolation<UpdateProductDto>> violations = validator.validate(dto);

        // Then
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Dado un UpdateProductDto con name vacío, cuando se valida, entonces no debe tener violaciones de validación")
    void givenUpdateProductDtoWithEmptyName_whenValidated_thenShouldNotHaveValidationViolations() {
        // Given
        UpdateProductDto dto = new UpdateProductDto(
                "", // Name vacío permitido en updates
                "https://example.com/image.jpg",
                "Description",
                new BigDecimal("100.00"),
                "USD",
                4.5,
                "Electronics",
                "Brand",
                Map.of("key", "value")
        );

        // When
        Set<ConstraintViolation<UpdateProductDto>> violations = validator.validate(dto);

        // Then
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Dado dos UpdateProductDto iguales, cuando se comparan, entonces deben ser iguales")
    void givenTwoEqualUpdateProductDtos_whenCompared_thenShouldBeEqual() {
        // Given
        UpdateProductDto dto1 = new UpdateProductDto(
                "Name",
                "https://example.com/image.jpg",
                "Description",
                new BigDecimal("100.00"),
                "USD",
                4.5,
                "Electronics",
                "Brand",
                Map.of("key", "value")
        );

        UpdateProductDto dto2 = new UpdateProductDto(
                "Name",
                "https://example.com/image.jpg",
                "Description",
                new BigDecimal("100.00"),
                "USD",
                4.5,
                "Electronics",
                "Brand",
                Map.of("key", "value")
        );

        // When & Then
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    @DisplayName("Dado dos UpdateProductDto diferentes, cuando se comparan, entonces no deben ser iguales")
    void givenTwoDifferentUpdateProductDtos_whenCompared_thenShouldNotBeEqual() {
        // Given
        UpdateProductDto dto1 = new UpdateProductDto(
                "Name",
                "https://example.com/image.jpg",
                "Description",
                new BigDecimal("100.00"),
                "USD",
                4.5,
                "Electronics",
                "Brand",
                Map.of("key", "value")
        );

        UpdateProductDto dto2 = new UpdateProductDto(
                "Different Name", // Diferente nombre
                "https://example.com/image.jpg",
                "Description",
                new BigDecimal("100.00"),
                "USD",
                4.5,
                "Electronics",
                "Brand",
                Map.of("key", "value")
        );

        // When & Then
        assertNotEquals(dto1, dto2);
        assertNotEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    @DisplayName("Dado un UpdateProductDto, cuando se convierte a string, entonces debe contener los campos relevantes")
    void givenUpdateProductDto_whenToString_thenShouldContainRelevantFields() {
        // Given
        UpdateProductDto dto = new UpdateProductDto(
                "Updated Product",
                "https://example.com/updated.jpg",
                "Updated description",
                new BigDecimal("150.00"),
                "EUR",
                4.8,
                "Updated Category",
                "Updated Brand",
                Map.of("new_key", "new_value")
        );

        // When
        String stringRepresentation = dto.toString();

        // Then
        assertThat(stringRepresentation)
                .contains("Updated Product")
                .contains("https://example.com/updated.jpg")
                .contains("EUR")
                .contains("4.8");
    }

    @Test
    @DisplayName("Dado un UpdateProductDto con solo un campo actualizado, cuando se valida, entonces no debe tener violaciones de validación")
    void givenUpdateProductDtoWithSingleFieldUpdate_whenValidated_thenShouldNotHaveValidationViolations() {
        // Given - Solo actualizando el precio
        UpdateProductDto dto = new UpdateProductDto(
                null, // name no se actualiza
                null, // imageUrl no se actualiza
                null, // description no se actualiza
                new BigDecimal("699.99"), // Solo precio actualizado
                null, // currency no se actualiza
                null, // rating no se actualiza
                null, // category no se actualiza
                null, // brand no se actualiza
                null  // specifications no se actualiza
        );

        // When
        Set<ConstraintViolation<UpdateProductDto>> violations = validator.validate(dto);

        // Then
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Dado un UpdateProductDto con specifications vacías, cuando se valida, entonces no debe tener violaciones de validación")
    void givenUpdateProductDtoWithEmptySpecifications_whenValidated_thenShouldNotHaveValidationViolations() {
        // Given
        UpdateProductDto dto = new UpdateProductDto(
                "Product Name",
                "https://example.com/image.jpg",
                "Description",
                new BigDecimal("100.00"),
                "USD",
                4.5,
                "Electronics",
                "Brand",
                Map.of() // Specifications vacías
        );

        // When
        Set<ConstraintViolation<UpdateProductDto>> violations = validator.validate(dto);

        // Then
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Dado un UpdateProductDto con valores extremos, cuando se valida, entonces no debe tener violaciones de validación")
    void givenUpdateProductDtoWithExtremeValues_whenValidated_thenShouldNotHaveValidationViolations() {
        // Given
        UpdateProductDto dto = new UpdateProductDto(
                "A".repeat(1000), // Nombre muy largo
                "https://example.com/image.jpg",
                "D".repeat(5000), // Descripción muy larga
                new BigDecimal("9999999.99"), // Precio muy alto
                "USD",
                10.0, // Rating máximo excedido
                "C".repeat(200), // Categoría muy larga
                "B".repeat(150), // Marca muy larga
                Map.of(
                        "key1", "value1",
                        "key2", "value2",
                        "key3", "value3"
                )
        );

        // When
        Set<ConstraintViolation<UpdateProductDto>> violations = validator.validate(dto);

        // Then
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Dado un UpdateProductDto con null en campos individuales, cuando se valida, entonces no debe tener violaciones de validación")
    void givenUpdateProductDtoWithIndividualNulls_whenValidated_thenShouldNotHaveValidationViolations() {
        // Given - Probando diferentes combinaciones de nulos
        UpdateProductDto[] dtos = {
                new UpdateProductDto(null, "url", "desc", BigDecimal.ONE, "USD", 4.5, "cat", "brand", Map.of()),
                new UpdateProductDto("name", null, "desc", BigDecimal.ONE, "USD", 4.5, "cat", "brand", Map.of()),
                new UpdateProductDto("name", "url", null, BigDecimal.ONE, "USD", 4.5, "cat", "brand", Map.of()),
                new UpdateProductDto("name", "url", "desc", null, "USD", 4.5, "cat", "brand", Map.of()),
                new UpdateProductDto("name", "url", "desc", BigDecimal.ONE, null, 4.5, "cat", "brand", Map.of()),
                new UpdateProductDto("name", "url", "desc", BigDecimal.ONE, "USD", null, "cat", "brand", Map.of()),
                new UpdateProductDto("name", "url", "desc", BigDecimal.ONE, "USD", 4.5, null, "brand", Map.of()),
                new UpdateProductDto("name", "url", "desc", BigDecimal.ONE, "USD", 4.5, "cat", null, Map.of()),
                new UpdateProductDto("name", "url", "desc", BigDecimal.ONE, "USD", 4.5, "cat", "brand", null)
        };

        // When & Then
        for (UpdateProductDto dto : dtos) {
            Set<ConstraintViolation<UpdateProductDto>> violations = validator.validate(dto);
            assertThat(violations).isEmpty();
        }
    }
}