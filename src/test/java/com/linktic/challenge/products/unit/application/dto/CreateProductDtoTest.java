package com.linktic.challenge.products.unit.application.dto;

import com.linktic.challenge.products.application.dto.CreateProductDto;
import com.linktic.challenge.products.objectmother.ProductObjectMother;
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
    @DisplayName("Dado un CreateProductDto válido desde ObjectMother, cuando se crea, entonces no debe tener violaciones de validación")
    void givenValidCreateProductDtoFromObjectMother_whenCreated_thenNoValidationViolations() {
        // Given
        CreateProductDto dto = ProductObjectMother.validCreateProductDto();

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
        // Given - Usando ObjectMother como base y modificando solo el campo necesario
        CreateProductDto baseDto = ProductObjectMother.validCreateProductDto();
        CreateProductDto dto = new CreateProductDto(
                baseDto.name(),
                invalidImageUrl,
                baseDto.description(),
                baseDto.price(),
                baseDto.currency(),
                baseDto.rating(),
                baseDto.category(),
                baseDto.brand(),
                baseDto.specifications()
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
    @DisplayName("Dado un CreateProductDto con rating nulo desde ObjectMother, cuando se valida, entonces no debe tener violación de validación")
    void givenCreateProductDtoWithNullRating_whenValidated_thenShouldNotHaveValidationViolation() {
        // Given - Usando ObjectMother como base
        CreateProductDto baseDto = ProductObjectMother.validCreateProductDto();
        CreateProductDto dto = new CreateProductDto(
                baseDto.name(),
                baseDto.imageUrl(),
                baseDto.description(),
                baseDto.price(),
                baseDto.currency(),
                null, // Rating nulo permitido
                baseDto.category(),
                baseDto.brand(),
                baseDto.specifications()
        );

        // When
        Set<ConstraintViolation<CreateProductDto>> violations = validator.validate(dto);

        // Then
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Dado un CreateProductDto con description nula desde ObjectMother, cuando se valida, entonces no debe tener violación de validación")
    void givenCreateProductDtoWithNullDescription_whenValidated_thenShouldNotHaveValidationViolation() {
        // Given
        CreateProductDto baseDto = ProductObjectMother.validCreateProductDto();
        CreateProductDto dto = new CreateProductDto(
                baseDto.name(),
                baseDto.imageUrl(),
                null, // Description nula permitida
                baseDto.price(),
                baseDto.currency(),
                baseDto.rating(),
                baseDto.category(),
                baseDto.brand(),
                baseDto.specifications()
        );

        // When
        Set<ConstraintViolation<CreateProductDto>> violations = validator.validate(dto);

        // Then
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Dado un CreateProductDto con category nula desde ObjectMother, cuando se valida, entonces no debe tener violación de validación")
    void givenCreateProductDtoWithNullCategory_whenValidated_thenShouldNotHaveValidationViolation() {
        // Given
        CreateProductDto baseDto = ProductObjectMother.validCreateProductDto();
        CreateProductDto dto = new CreateProductDto(
                baseDto.name(),
                baseDto.imageUrl(),
                baseDto.description(),
                baseDto.price(),
                baseDto.currency(),
                baseDto.rating(),
                null, // Category nula permitida
                baseDto.brand(),
                baseDto.specifications()
        );

        // When
        Set<ConstraintViolation<CreateProductDto>> violations = validator.validate(dto);

        // Then
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Dado un CreateProductDto con brand nula desde ObjectMother, cuando se valida, entonces no debe tener violación de validación")
    void givenCreateProductDtoWithNullBrand_whenValidated_thenShouldNotHaveValidationViolation() {
        // Given
        CreateProductDto baseDto = ProductObjectMother.validCreateProductDto();
        CreateProductDto dto = new CreateProductDto(
                baseDto.name(),
                baseDto.imageUrl(),
                baseDto.description(),
                baseDto.price(),
                baseDto.currency(),
                baseDto.rating(),
                baseDto.category(),
                null, // Brand nula permitida
                baseDto.specifications()
        );

        // When
        Set<ConstraintViolation<CreateProductDto>> violations = validator.validate(dto);

        // Then
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Dado un CreateProductDto con specifications nulas desde ObjectMother, cuando se valida, entonces no debe tener violación de validación")
    void givenCreateProductDtoWithNullSpecifications_whenValidated_thenShouldNotHaveValidationViolation() {
        // Given
        CreateProductDto baseDto = ProductObjectMother.validCreateProductDto();
        CreateProductDto dto = new CreateProductDto(
                baseDto.name(),
                baseDto.imageUrl(),
                baseDto.description(),
                baseDto.price(),
                baseDto.currency(),
                baseDto.rating(),
                baseDto.category(),
                baseDto.brand(),
                null // Specifications nulas permitidas
        );

        // When
        Set<ConstraintViolation<CreateProductDto>> violations = validator.validate(dto);

        // Then
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Dado un CreateProductDto con specifications vacías desde ObjectMother, cuando se valida, entonces no debe tener violación de validación")
    void givenCreateProductDtoWithEmptySpecifications_whenValidated_thenShouldNotHaveValidationViolation() {
        // Given
        CreateProductDto baseDto = ProductObjectMother.validCreateProductDto();
        CreateProductDto dto = new CreateProductDto(
                baseDto.name(),
                baseDto.imageUrl(),
                baseDto.description(),
                baseDto.price(),
                baseDto.currency(),
                baseDto.rating(),
                baseDto.category(),
                baseDto.brand(),
                Map.of() // Specifications vacías permitidas
        );

        // When
        Set<ConstraintViolation<CreateProductDto>> violations = validator.validate(dto);

        // Then
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Dado un CreateProductDto con price cero desde ObjectMother, cuando se valida, entonces no debe tener violación de validación")
    void givenCreateProductDtoWithZeroPrice_whenValidated_thenShouldNotHaveValidationViolation() {
        // Given
        CreateProductDto baseDto = ProductObjectMother.validCreateProductDto();
        CreateProductDto dto = new CreateProductDto(
                baseDto.name(),
                baseDto.imageUrl(),
                baseDto.description(),
                BigDecimal.ZERO,
                baseDto.currency(),
                baseDto.rating(),
                baseDto.category(),
                baseDto.brand(),
                baseDto.specifications()
        );

        // When
        Set<ConstraintViolation<CreateProductDto>> violations = validator.validate(dto);

        // Then
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Dado un CreateProductDto con rating en los límites, cuando se valida, entonces no debe tener violación de validación")
    void givenCreateProductDtoWithBoundaryRating_whenValidated_thenShouldNotHaveValidationViolation() {
        // Given - Usando ObjectMother como base
        CreateProductDto baseDto = ProductObjectMother.validCreateProductDto();

        CreateProductDto dtoMin = new CreateProductDto(
                baseDto.name(),
                baseDto.imageUrl(),
                baseDto.description(),
                baseDto.price(),
                baseDto.currency(),
                0.0, // Mínimo permitido
                baseDto.category(),
                baseDto.brand(),
                baseDto.specifications()
        );

        CreateProductDto dtoMax = new CreateProductDto(
                baseDto.name(),
                baseDto.imageUrl(),
                baseDto.description(),
                baseDto.price(),
                baseDto.currency(),
                5.0, // Máximo permitido
                baseDto.category(),
                baseDto.brand(),
                baseDto.specifications()
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
        CreateProductDto baseDto = ProductObjectMother.validCreateProductDto();
        CreateProductDto dto = new CreateProductDto(
                baseDto.name(),
                validImageUrl,
                baseDto.description(),
                baseDto.price(),
                baseDto.currency(),
                baseDto.rating(),
                baseDto.category(),
                baseDto.brand(),
                baseDto.specifications()
        );

        // When
        Set<ConstraintViolation<CreateProductDto>> violations = validator.validate(dto);

        // Then
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Dado dos CreateProductDto iguales desde ObjectMother, cuando se comparan, entonces deben ser iguales")
    void givenTwoEqualCreateProductDtos_whenCompared_thenShouldBeEqual() {
        // Given
        CreateProductDto dto1 = ProductObjectMother.validCreateProductDto();
        CreateProductDto dto2 = ProductObjectMother.validCreateProductDto();

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
        assertThat(violations)
                .extracting(v -> v.getPropertyPath().toString())
                .containsExactlyInAnyOrder("name", "imageUrl", "currency");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "  "})
    @DisplayName("Dado un CreateProductDto con name nulo o vacío, cuando se valida, entonces debe tener violación de validación")
    void givenCreateProductDtoWithInvalidName_whenValidated_thenShouldHaveValidationViolation(String invalidName) {
        // Given
        CreateProductDto baseDto = ProductObjectMother.validCreateProductDto();
        CreateProductDto dto = new CreateProductDto(
                invalidName,
                baseDto.imageUrl(),
                baseDto.description(),
                baseDto.price(),
                baseDto.currency(),
                baseDto.rating(),
                baseDto.category(),
                baseDto.brand(),
                baseDto.specifications()
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
        CreateProductDto baseDto = ProductObjectMother.validCreateProductDto();
        CreateProductDto dto = new CreateProductDto(
                baseDto.name(),
                baseDto.imageUrl(),
                baseDto.description(),
                new BigDecimal(negativePrice),
                baseDto.currency(),
                baseDto.rating(),
                baseDto.category(),
                baseDto.brand(),
                baseDto.specifications()
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
        CreateProductDto baseDto = ProductObjectMother.validCreateProductDto();
        CreateProductDto dto = new CreateProductDto(
                baseDto.name(),
                baseDto.imageUrl(),
                baseDto.description(),
                baseDto.price(),
                invalidCurrency,
                baseDto.rating(),
                baseDto.category(),
                baseDto.brand(),
                baseDto.specifications()
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
        CreateProductDto baseDto = ProductObjectMother.validCreateProductDto();
        CreateProductDto dto = new CreateProductDto(
                baseDto.name(),
                baseDto.imageUrl(),
                baseDto.description(),
                baseDto.price(),
                baseDto.currency(),
                invalidRating,
                baseDto.category(),
                baseDto.brand(),
                baseDto.specifications()
        );

        // When
        Set<ConstraintViolation<CreateProductDto>> violations = validator.validate(dto);

        // Then
        assertThat(violations)
                .isNotEmpty()
                .anyMatch(v -> v.getPropertyPath().toString().equals("rating"));
    }

    @Test
    @DisplayName("Dado un CreateProductDto desde ObjectMother, cuando se convierte a string, entonces debe contener los campos relevantes")
    void givenCreateProductDtoFromObjectMother_whenToString_thenShouldContainRelevantFields() {
        // Given
        CreateProductDto dto = ProductObjectMother.validCreateProductDto();

        // When
        String stringRepresentation = dto.toString();

        // Then
        assertThat(stringRepresentation)
                .contains("Smartphone Galaxy XZ")
                .contains("https://example.com/galaxy-xz.jpg")
                .contains("USD");
    }

    @Test
    @DisplayName("Dado un CreateProductDto con campos opcionales nulos desde ObjectMother, cuando se valida, entonces no debe tener violaciones")
    void givenCreateProductDtoWithOptionalNullsFromObjectMother_whenValidated_thenShouldNotHaveViolations() {
        // Given - Crear un DTO con todos los campos opcionales nulos
        CreateProductDto dto = new CreateProductDto(
                "Valid Product Name",
                "https://example.com/image.jpg",
                null, // description opcional
                new BigDecimal("100.00"),
                "USD",
                null, // rating opcional
                null, // category opcional
                null, // brand opcional
                null  // specifications opcional
        );

        // When
        Set<ConstraintViolation<CreateProductDto>> violations = validator.validate(dto);

        // Then
        assertThat(violations).isEmpty();
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
}