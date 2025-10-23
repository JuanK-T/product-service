package com.linktic.challenge.products.unit.application.dto;

import com.linktic.challenge.products.application.dto.CreateProductDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class CreateProductDtoTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @Test
    @DisplayName("Dado un CreateProductDto válido, cuando se crea, entonces no debe tener violaciones de validación")
    void givenValidCreateProductDto_whenCreated_thenNoValidationViolations() {
        // Given
        CreateProductDto dto = createValidCreateProductDto();

        // When
        Set<ConstraintViolation<CreateProductDto>> violations = validator.validate(dto);

        // Then
        assertThat(violations).isEmpty();
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "  ", "invalid-url", "ftp://example.com", "www.example.com"})
    @DisplayName("Dado un CreateProductDto con imageUrl inválida, cuando se valida, entonces debe tener violación de validación")
    void givenCreateProductDtoWithInvalidImageUrl_whenValidated_thenShouldHaveValidationViolation(String invalidImageUrl) {
        // Given
        CreateProductDto dto = new CreateProductDto(
                "Valid Product Name",
                invalidImageUrl,
                "Valid description",
                new BigDecimal("100.00"),
                "USD",
                4.5,
                "Electronics",
                "Brand",
                Map.of("key", "value")
        );

        // When
        Set<ConstraintViolation<CreateProductDto>> violations = validator.validate(dto);

        // Then
        assertThat(violations).isNotEmpty();
        if (invalidImageUrl != null && !invalidImageUrl.trim().isEmpty()) {
            assertThat(violations).anyMatch(v ->
                    v.getPropertyPath().toString().equals("imageUrl")
            );
        } else {
            assertThat(violations).anyMatch(v ->
                    v.getPropertyPath().toString().equals("imageUrl") &&
                            v.getMessage().contains("no debe estar vacío")
            );
        }
    }

    @Test
    @DisplayName("Dado un CreateProductDto con rating nulo, cuando se valida, entonces no debe tener violación de validación")
    void givenCreateProductDtoWithNullRating_whenValidated_thenShouldNotHaveValidationViolation() {
        // Given
        CreateProductDto dto = new CreateProductDto(
                "Valid Product Name",
                "https://example.com/image.jpg",
                "Valid description",
                new BigDecimal("100.00"),
                "USD",
                null, // Rating nulo permitido
                "Electronics",
                "Brand",
                Map.of("key", "value")
        );

        // When
        Set<ConstraintViolation<CreateProductDto>> violations = validator.validate(dto);

        // Then
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Dado un CreateProductDto con description nula, cuando se valida, entonces no debe tener violación de validación")
    void givenCreateProductDtoWithNullDescription_whenValidated_thenShouldNotHaveValidationViolation() {
        // Given
        CreateProductDto dto = new CreateProductDto(
                "Valid Product Name",
                "https://example.com/image.jpg",
                null, // Description nula permitida
                new BigDecimal("100.00"),
                "USD",
                4.5,
                "Electronics",
                "Brand",
                Map.of("key", "value")
        );

        // When
        Set<ConstraintViolation<CreateProductDto>> violations = validator.validate(dto);

        // Then
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Dado un CreateProductDto con category nula, cuando se valida, entonces no debe tener violación de validación")
    void givenCreateProductDtoWithNullCategory_whenValidated_thenShouldNotHaveValidationViolation() {
        // Given
        CreateProductDto dto = new CreateProductDto(
                "Valid Product Name",
                "https://example.com/image.jpg",
                "Valid description",
                new BigDecimal("100.00"),
                "USD",
                4.5,
                null, // Category nula permitida
                "Brand",
                Map.of("key", "value")
        );

        // When
        Set<ConstraintViolation<CreateProductDto>> violations = validator.validate(dto);

        // Then
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Dado un CreateProductDto con brand nula, cuando se valida, entonces no debe tener violación de validación")
    void givenCreateProductDtoWithNullBrand_whenValidated_thenShouldNotHaveValidationViolation() {
        // Given
        CreateProductDto dto = new CreateProductDto(
                "Valid Product Name",
                "https://example.com/image.jpg",
                "Valid description",
                new BigDecimal("100.00"),
                "USD",
                4.5,
                "Electronics",
                null, // Brand nula permitida
                Map.of("key", "value")
        );

        // When
        Set<ConstraintViolation<CreateProductDto>> violations = validator.validate(dto);

        // Then
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Dado un CreateProductDto con specifications nulas, cuando se valida, entonces no debe tener violación de validación")
    void givenCreateProductDtoWithNullSpecifications_whenValidated_thenShouldNotHaveValidationViolation() {
        // Given
        CreateProductDto dto = new CreateProductDto(
                "Valid Product Name",
                "https://example.com/image.jpg",
                "Valid description",
                new BigDecimal("100.00"),
                "USD",
                4.5,
                "Electronics",
                "Brand",
                null // Specifications nulas permitidas
        );

        // When
        Set<ConstraintViolation<CreateProductDto>> violations = validator.validate(dto);

        // Then
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Dado un CreateProductDto con specifications vacías, cuando se valida, entonces no debe tener violación de validación")
    void givenCreateProductDtoWithEmptySpecifications_whenValidated_thenShouldNotHaveValidationViolation() {
        // Given
        CreateProductDto dto = new CreateProductDto(
                "Valid Product Name",
                "https://example.com/image.jpg",
                "Valid description",
                new BigDecimal("100.00"),
                "USD",
                4.5,
                "Electronics",
                "Brand",
                Map.of() // Specifications vacías permitidas
        );

        // When
        Set<ConstraintViolation<CreateProductDto>> violations = validator.validate(dto);

        // Then
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Dado un CreateProductDto con price cero, cuando se valida, entonces no debe tener violación de validación")
    void givenCreateProductDtoWithZeroPrice_whenValidated_thenShouldNotHaveValidationViolation() {
        // Given
        CreateProductDto dto = new CreateProductDto(
                "Valid Product Name",
                "https://example.com/image.jpg",
                "Valid description",
                BigDecimal.ZERO,
                "USD",
                4.5,
                "Electronics",
                "Brand",
                Map.of("key", "value")
        );

        // When
        Set<ConstraintViolation<CreateProductDto>> violations = validator.validate(dto);

        // Then
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Dado un CreateProductDto con rating en los límites, cuando se valida, entonces no debe tener violación de validación")
    void givenCreateProductDtoWithBoundaryRating_whenValidated_thenShouldNotHaveValidationViolation() {
        // Given
        CreateProductDto dtoMin = new CreateProductDto(
                "Valid Product Name",
                "https://example.com/image.jpg",
                "Valid description",
                new BigDecimal("100.00"),
                "USD",
                0.0, // Mínimo permitido
                "Electronics",
                "Brand",
                Map.of("key", "value")
        );

        CreateProductDto dtoMax = new CreateProductDto(
                "Valid Product Name",
                "https://example.com/image.jpg",
                "Valid description",
                new BigDecimal("100.00"),
                "USD",
                5.0, // Máximo permitido
                "Electronics",
                "Brand",
                Map.of("key", "value")
        );

        // When & Then
        assertThat(validator.validate(dtoMin)).isEmpty();
        assertThat(validator.validate(dtoMax)).isEmpty();
    }

    @ParameterizedTest
    @MethodSource("validImageUrlsProvider")
    @DisplayName("Dado un CreateProductDto con imageUrl válida, cuando se valida, entonces no debe tener violación de validación")
    void givenCreateProductDtoWithValidImageUrl_whenValidated_thenShouldNotHaveValidationViolation(String validImageUrl) {
        // Given
        CreateProductDto dto = new CreateProductDto(
                "Valid Product Name",
                validImageUrl,
                "Valid description",
                new BigDecimal("100.00"),
                "USD",
                4.5,
                "Electronics",
                "Brand",
                Map.of("key", "value")
        );

        // When
        Set<ConstraintViolation<CreateProductDto>> violations = validator.validate(dto);

        // Then
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Dado dos CreateProductDto iguales, cuando se comparan, entonces deben ser iguales")
    void givenTwoEqualCreateProductDtos_whenCompared_thenShouldBeEqual() {
        // Given
        CreateProductDto dto1 = createValidCreateProductDto();
        CreateProductDto dto2 = createValidCreateProductDto();

        // When & Then
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    @DisplayName("Dado un CreateProductDto con todos los campos nulos permitidos, cuando se valida, entonces debe tener violaciones solo en campos requeridos")
    void givenCreateProductDtoWithAllNullsAllowed_whenValidated_thenShouldHaveViolationsOnlyForRequiredFields() {
        // Given
        CreateProductDto dto = new CreateProductDto(
                null, // Requerido
                null, // Requerido
                null, // Opcional
                null, // Requerido (pero la validación es solo para mínimo)
                null, // Requerido
                null, // Opcional
                null, // Opcional
                null, // Opcional
                null  // Opcional
        );

        // When
        Set<ConstraintViolation<CreateProductDto>> violations = validator.validate(dto);

        // Then
        assertThat(violations).isNotEmpty();

        // ✅ CORRECCIÓN: Usar función lambda en lugar de string
        assertThat(violations)
                .extracting(v -> v.getPropertyPath().toString())
                .containsExactlyInAnyOrder("name", "imageUrl", "currency");
    }

    private static Stream<Arguments> validImageUrlsProvider() {
        return Stream.of(
                Arguments.of("https://example.com/image.jpg"),
                Arguments.of("http://example.com/image.png"),
                Arguments.of("https://sub.domain.example.com/path/to/image.jpeg"),
                Arguments.of("http://localhost:8080/images/product.jpg"),
                Arguments.of("https://example.com/image.jpg?width=200&height=200"),
                Arguments.of("http://example.com/image.png#section")
        );
    }

    private CreateProductDto createValidCreateProductDto() {
        return new CreateProductDto(
                "Smartphone Galaxy XZ",
                "https://example.com/image.jpg",
                "High-end smartphone with AMOLED display",
                new BigDecimal("899.99"),
                "USD",
                4.7,
                "Electronics",
                "TechNova",
                Map.of(
                        "screen", "6.5 inches AMOLED",
                        "processor", "Snapdragon 8 Gen 2",
                        "memory", "256GB"
                )
        );
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "  "})
    @DisplayName("Dado un CreateProductDto con name nulo o vacío, cuando se valida, entonces debe tener violación de validación")
    void givenCreateProductDtoWithInvalidName_whenValidated_thenShouldHaveValidationViolation(String invalidName) {
        // Given
        CreateProductDto dto = new CreateProductDto(
                invalidName,
                "https://example.com/image.jpg",
                "Valid description",
                new BigDecimal("100.00"),
                "USD",
                4.5,
                "Electronics",
                "Brand",
                Map.of("key", "value")
        );

        // When
        Set<ConstraintViolation<CreateProductDto>> violations = validator.validate(dto);

        // Then
        assertThat(violations)
                .isNotEmpty()
                .anyMatch(v -> v.getPropertyPath().toString().equals("name") &&
                        v.getMessage().contains("no debe estar vacío"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"-1.0", "-0.01"})
    @DisplayName("Dado un CreateProductDto con price negativo, cuando se valida, entonces debe tener violación de validación")
    void givenCreateProductDtoWithNegativePrice_whenValidated_thenShouldHaveValidationViolation(String negativePrice) {
        // Given
        CreateProductDto dto = new CreateProductDto(
                "Valid Product Name",
                "https://example.com/image.jpg",
                "Valid description",
                new BigDecimal(negativePrice),
                "USD",
                4.5,
                "Electronics",
                "Brand",
                Map.of("key", "value")
        );

        // When
        Set<ConstraintViolation<CreateProductDto>> violations = validator.validate(dto);

        // Then
        assertThat(violations)
                .isNotEmpty()
                .anyMatch(v -> v.getPropertyPath().toString().equals("price") &&
                        v.getMessage().contains("0.0"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "  "})
    @DisplayName("Dado un CreateProductDto con currency nula o vacía, cuando se valida, entonces debe tener violación de validación")
    void givenCreateProductDtoWithInvalidCurrency_whenValidated_thenShouldHaveValidationViolation(String invalidCurrency) {
        // Given
        CreateProductDto dto = new CreateProductDto(
                "Valid Product Name",
                "https://example.com/image.jpg",
                "Valid description",
                new BigDecimal("100.00"),
                invalidCurrency,
                4.5,
                "Electronics",
                "Brand",
                Map.of("key", "value")
        );

        // When
        Set<ConstraintViolation<CreateProductDto>> violations = validator.validate(dto);

        // Then
        assertThat(violations)
                .isNotEmpty()
                .anyMatch(v -> v.getPropertyPath().toString().equals("currency") &&
                        v.getMessage().contains("no debe estar vacío"));
    }

    @ParameterizedTest
    @ValueSource(doubles = {-0.1, -1.0, 5.1, 6.0, 10.0})
    @DisplayName("Dado un CreateProductDto con rating fuera de rango, cuando se valida, entonces debe tener violación de validación")
    void givenCreateProductDtoWithOutOfRangeRating_whenValidated_thenShouldHaveValidationViolation(double invalidRating) {
        // Given
        CreateProductDto dto = new CreateProductDto(
                "Valid Product Name",
                "https://example.com/image.jpg",
                "Valid description",
                new BigDecimal("100.00"),
                "USD",
                invalidRating,
                "Electronics",
                "Brand",
                Map.of("key", "value")
        );

        // When
        Set<ConstraintViolation<CreateProductDto>> violations = validator.validate(dto);

        // Then
        assertThat(violations)
                .isNotEmpty()
                .anyMatch(v -> v.getPropertyPath().toString().equals("rating"));
    }

    @Test
    @DisplayName("Dado un CreateProductDto, cuando se convierte a string, entonces debe contener los campos relevantes")
    void givenCreateProductDto_whenToString_thenShouldContainRelevantFields() {
        // Given
        CreateProductDto dto = createValidCreateProductDto();

        // When
        String stringRepresentation = dto.toString();

        // Then
        assertThat(stringRepresentation)
                .contains("Smartphone Galaxy XZ")
                .contains("https://example.com/image.jpg")
                .contains("USD");
    }
}