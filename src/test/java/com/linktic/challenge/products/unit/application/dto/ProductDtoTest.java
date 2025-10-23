package com.linktic.challenge.products.unit.application.dto;

import com.linktic.challenge.products.application.dto.ProductDto;
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

class ProductDtoTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @Test
    @DisplayName("Dado un ProductDto válido desde ObjectMother, cuando se crea, entonces no debe tener violaciones de validación")
    void givenValidProductDtoFromObjectMother_whenCreated_thenNoValidationViolations() {
        // Given
        ProductDto dto = ProductObjectMother.validProductDto();

        // When
        Set<ConstraintViolation<ProductDto>> violations = validator.validate(dto);

        // Then
        assertThat(violations).isEmpty();
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "  "})
    @DisplayName("Dado un ProductDto con id nulo o vacío, cuando se valida, entonces debe tener violación de validación")
    void givenProductDtoWithInvalidId_whenValidated_thenShouldHaveValidationViolation(String invalidId) {
        // Given
        ProductDto baseDto = ProductObjectMother.validProductDto();
        ProductDto dto = new ProductDto(
                invalidId,
                baseDto.name(),
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
        Set<ConstraintViolation<ProductDto>> violations = validator.validate(dto);

        // Then
        assertThat(violations)
                .isNotEmpty()
                .anyMatch(v -> v.getPropertyPath().toString().equals("id") &&
                        v.getMessage().contains("no debe estar vacío"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "  "})
    @DisplayName("Dado un ProductDto con name nulo o vacío, cuando se valida, entonces debe tener violación de validación")
    void givenProductDtoWithInvalidName_whenValidated_thenShouldHaveValidationViolation(String invalidName) {
        // Given
        ProductDto baseDto = ProductObjectMother.validProductDto();
        ProductDto dto = new ProductDto(
                baseDto.id(),
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
        Set<ConstraintViolation<ProductDto>> violations = validator.validate(dto);

        // Then
        assertThat(violations)
                .isNotEmpty()
                .anyMatch(v -> v.getPropertyPath().toString().equals("name") &&
                        v.getMessage().contains("no debe estar vacío"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "  ", "invalid-url", "ftp://example.com", "www.example.com"})
    @DisplayName("Dado un ProductDto con imageUrl inválida, cuando se valida, entonces debe tener violación de validación")
    void givenProductDtoWithInvalidImageUrl_whenValidated_thenShouldHaveValidationViolation(String invalidImageUrl) {
        // Given
        ProductDto baseDto = ProductObjectMother.validProductDto();
        ProductDto dto = new ProductDto(
                baseDto.id(),
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
        Set<ConstraintViolation<ProductDto>> violations = validator.validate(dto);

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

    @ParameterizedTest
    @ValueSource(strings = {"-1.0", "-0.01"})
    @DisplayName("Dado un ProductDto con price negativo, cuando se valida, entonces debe tener violación de validación")
    void givenProductDtoWithNegativePrice_whenValidated_thenShouldHaveValidationViolation(String negativePrice) {
        // Given
        ProductDto baseDto = ProductObjectMother.validProductDto();
        ProductDto dto = new ProductDto(
                baseDto.id(),
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
        Set<ConstraintViolation<ProductDto>> violations = validator.validate(dto);

        // Then
        assertThat(violations)
                .isNotEmpty()
                .anyMatch(v -> v.getPropertyPath().toString().equals("price") &&
                        v.getMessage().contains("0.0"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "  "})
    @DisplayName("Dado un ProductDto con currency nula o vacía, cuando se valida, entonces debe tener violación de validación")
    void givenProductDtoWithInvalidCurrency_whenValidated_thenShouldHaveValidationViolation(String invalidCurrency) {
        // Given
        ProductDto baseDto = ProductObjectMother.validProductDto();
        ProductDto dto = new ProductDto(
                baseDto.id(),
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
        Set<ConstraintViolation<ProductDto>> violations = validator.validate(dto);

        // Then
        assertThat(violations)
                .isNotEmpty()
                .anyMatch(v -> v.getPropertyPath().toString().equals("currency") &&
                        v.getMessage().contains("no debe estar vacío"));
    }

    @ParameterizedTest
    @ValueSource(doubles = {-0.1, -1.0, 5.1, 6.0, 10.0})
    @DisplayName("Dado un ProductDto con rating fuera de rango, cuando se valida, entonces debe tener violación de validación")
    void givenProductDtoWithOutOfRangeRating_whenValidated_thenShouldHaveValidationViolation(double invalidRating) {
        // Given
        ProductDto baseDto = ProductObjectMother.validProductDto();
        ProductDto dto = new ProductDto(
                baseDto.id(),
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
        Set<ConstraintViolation<ProductDto>> violations = validator.validate(dto);

        // Then
        assertThat(violations)
                .isNotEmpty()
                .anyMatch(v -> v.getPropertyPath().toString().equals("rating"));
    }

    @Test
    @DisplayName("Dado un ProductDto con rating nulo desde ObjectMother, cuando se valida, entonces no debe tener violación de validación")
    void givenProductDtoWithNullRating_whenValidated_thenShouldNotHaveValidationViolation() {
        // Given
        ProductDto baseDto = ProductObjectMother.validProductDto();
        ProductDto dto = new ProductDto(
                baseDto.id(),
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
        Set<ConstraintViolation<ProductDto>> violations = validator.validate(dto);

        // Then
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Dado un ProductDto con description nula desde ObjectMother, cuando se valida, entonces no debe tener violación de validación")
    void givenProductDtoWithNullDescription_whenValidated_thenShouldNotHaveValidationViolation() {
        // Given
        ProductDto baseDto = ProductObjectMother.validProductDto();
        ProductDto dto = new ProductDto(
                baseDto.id(),
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
        Set<ConstraintViolation<ProductDto>> violations = validator.validate(dto);

        // Then
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Dado un ProductDto con category nula desde ObjectMother, cuando se valida, entonces no debe tener violación de validación")
    void givenProductDtoWithNullCategory_whenValidated_thenShouldNotHaveValidationViolation() {
        // Given
        ProductDto baseDto = ProductObjectMother.validProductDto();
        ProductDto dto = new ProductDto(
                baseDto.id(),
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
        Set<ConstraintViolation<ProductDto>> violations = validator.validate(dto);

        // Then
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Dado un ProductDto con brand nula desde ObjectMother, cuando se valida, entonces no debe tener violación de validación")
    void givenProductDtoWithNullBrand_whenValidated_thenShouldNotHaveValidationViolation() {
        // Given
        ProductDto baseDto = ProductObjectMother.validProductDto();
        ProductDto dto = new ProductDto(
                baseDto.id(),
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
        Set<ConstraintViolation<ProductDto>> violations = validator.validate(dto);

        // Then
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Dado un ProductDto con specifications nulas desde ObjectMother, cuando se valida, entonces no debe tener violación de validación")
    void givenProductDtoWithNullSpecifications_whenValidated_thenShouldNotHaveValidationViolation() {
        // Given
        ProductDto baseDto = ProductObjectMother.validProductDto();
        ProductDto dto = new ProductDto(
                baseDto.id(),
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
        Set<ConstraintViolation<ProductDto>> violations = validator.validate(dto);

        // Then
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Dado un ProductDto con specifications vacías desde ObjectMother, cuando se valida, entonces no debe tener violación de validación")
    void givenProductDtoWithEmptySpecifications_whenValidated_thenShouldNotHaveValidationViolation() {
        // Given
        ProductDto baseDto = ProductObjectMother.validProductDto();
        ProductDto dto = new ProductDto(
                baseDto.id(),
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
        Set<ConstraintViolation<ProductDto>> violations = validator.validate(dto);

        // Then
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Dado un ProductDto con price cero desde ObjectMother, cuando se valida, entonces no debe tener violación de validación")
    void givenProductDtoWithZeroPrice_whenValidated_thenShouldNotHaveValidationViolation() {
        // Given
        ProductDto baseDto = ProductObjectMother.validProductDto();
        ProductDto dto = new ProductDto(
                baseDto.id(),
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
        Set<ConstraintViolation<ProductDto>> violations = validator.validate(dto);

        // Then
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Dado un ProductDto con rating en los límites, cuando se valida, entonces no debe tener violación de validación")
    void givenProductDtoWithBoundaryRating_whenValidated_thenShouldNotHaveValidationViolation() {
        // Given
        ProductDto baseDto = ProductObjectMother.validProductDto();

        ProductDto dtoMin = new ProductDto(
                baseDto.id(),
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

        ProductDto dtoMax = new ProductDto(
                baseDto.id(),
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
    @DisplayName("Dado un ProductDto con imageUrl válida, cuando se valida, entonces no debe tener violación de validación")
    void givenProductDtoWithValidImageUrl_whenValidated_thenShouldNotHaveValidationViolation(String validImageUrl) {
        // Given
        ProductDto baseDto = ProductObjectMother.validProductDto();
        ProductDto dto = new ProductDto(
                baseDto.id(),
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
        Set<ConstraintViolation<ProductDto>> violations = validator.validate(dto);

        // Then
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Dado dos ProductDto iguales desde ObjectMother, cuando se comparan, entonces deben ser iguales")
    void givenTwoEqualProductDtos_whenCompared_thenShouldBeEqual() {
        // Given
        ProductDto dto1 = ProductObjectMother.validProductDto();
        ProductDto dto2 = ProductObjectMother.validProductDto();

        // When & Then
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    @DisplayName("Dado dos ProductDto diferentes, cuando se comparan, entonces no deben ser iguales")
    void givenTwoDifferentProductDtos_whenCompared_thenShouldNotBeEqual() {
        // Given
        ProductDto dto1 = ProductObjectMother.validProductDto();
        ProductDto dto2 = new ProductDto(
                "different-id", // Diferente ID
                dto1.name(),
                dto1.imageUrl(),
                dto1.description(),
                dto1.price(),
                dto1.currency(),
                dto1.rating(),
                dto1.category(),
                dto1.brand(),
                dto1.specifications()
        );

        // When & Then
        assertNotEquals(dto1, dto2);
        assertNotEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    @DisplayName("Dado un ProductDto desde ObjectMother, cuando se convierte a string, entonces debe contener los campos relevantes")
    void givenProductDto_whenToString_thenShouldContainRelevantFields() {
        // Given
        ProductDto dto = ProductObjectMother.validProductDto();

        // When
        String stringRepresentation = dto.toString();

        // Then
        assertThat(stringRepresentation)
                .contains("prod001")
                .contains("Smartphone Galaxy XZ")
                .contains("https://example.com/galaxy-xz.jpg")
                .contains("USD");
    }

    @Test
    @DisplayName("Dado un ProductDto con todos los campos nulos permitidos, cuando se valida, entonces debe tener violaciones solo en campos requeridos")
    void givenProductDtoWithAllNullsAllowed_whenValidated_thenShouldHaveViolationsOnlyForRequiredFields() {
        // Given
        ProductDto dto = new ProductDto(
                null, // Requerido
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
        Set<ConstraintViolation<ProductDto>> violations = validator.validate(dto);

        // Then
        assertThat(violations).isNotEmpty();
        assertThat(violations)
                .extracting(v -> v.getPropertyPath().toString())
                .containsExactlyInAnyOrder("id", "name", "imageUrl", "currency");
    }

    @Test
    @DisplayName("Dado un ProductDto con diferentes monedas válidas, cuando se valida, entonces no debe tener violación de validación")
    void givenProductDtoWithDifferentValidCurrencies_whenValidated_thenShouldNotHaveValidationViolation() {
        // Given
        ProductDto baseDto = ProductObjectMother.validProductDto();

        ProductDto dtoUSD = new ProductDto(
                baseDto.id(),
                baseDto.name(),
                baseDto.imageUrl(),
                baseDto.description(),
                baseDto.price(),
                "USD", // Dólar americano
                baseDto.rating(),
                baseDto.category(),
                baseDto.brand(),
                baseDto.specifications()
        );

        ProductDto dtoEUR = new ProductDto(
                "prod002",
                baseDto.name(),
                baseDto.imageUrl(),
                baseDto.description(),
                new BigDecimal("89.99"),
                "EUR", // Euro
                baseDto.rating(),
                baseDto.category(),
                baseDto.brand(),
                baseDto.specifications()
        );

        ProductDto dtoCOP = new ProductDto(
                "prod003",
                baseDto.name(),
                baseDto.imageUrl(),
                baseDto.description(),
                new BigDecimal("389000.00"),
                "COP", // Peso colombiano
                baseDto.rating(),
                baseDto.category(),
                baseDto.brand(),
                baseDto.specifications()
        );

        // When & Then
        assertThat(validator.validate(dtoUSD)).isEmpty();
        assertThat(validator.validate(dtoEUR)).isEmpty();
        assertThat(validator.validate(dtoCOP)).isEmpty();
    }

    @Test
    @DisplayName("Dado un ProductDto con valores extremos, cuando se valida, entonces debe comportarse correctamente")
    void givenProductDtoWithExtremeValues_whenValidated_thenShouldBehaveCorrectly() {
        // Given - Producto con precio muy alto
        ProductDto baseDto = ProductObjectMother.validProductDto();
        ProductDto dtoHighPrice = new ProductDto(
                baseDto.id(),
                "Luxury Product",
                baseDto.imageUrl(),
                "Very expensive product",
                new BigDecimal("999999.99"), // Precio muy alto
                baseDto.currency(),
                5.0, // Rating máximo
                "Luxury",
                "LuxuryBrand",
                baseDto.specifications()
        );

        // Given - Producto con descripción larga
        ProductDto dtoLongDescription = new ProductDto(
                "prod002",
                "Product with Long Description",
                baseDto.imageUrl(),
                "This is a very long description that might exceed typical length but should still be valid for the DTO validation since there are no length constraints defined in the annotations", // Descripción larga
                new BigDecimal("50.00"),
                baseDto.currency(),
                3.5,
                "General",
                "GenericBrand",
                baseDto.specifications()
        );

        // When & Then
        assertThat(validator.validate(dtoHighPrice)).isEmpty();
        assertThat(validator.validate(dtoLongDescription)).isEmpty();
    }

    @Test
    @DisplayName("Dado un ProductDto con specifications complejas, cuando se valida, entonces no debe tener violación de validación")
    void givenProductDtoWithComplexSpecifications_whenValidated_thenShouldNotHaveValidationViolation() {
        // Given
        Map<String, String> complexSpecs = Map.of(
        );

        ProductDto baseDto = ProductObjectMother.validProductDto();
        ProductDto dto = new ProductDto(
                baseDto.id(),
                "Advanced Smartphone",
                baseDto.imageUrl(),
                "Feature-packed smartphone",
                baseDto.price(),
                baseDto.currency(),
                baseDto.rating(),
                baseDto.category(),
                baseDto.brand(),
                complexSpecs
        );

        // When
        Set<ConstraintViolation<ProductDto>> violations = validator.validate(dto);

        // Then
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Dado un ProductDto con campos opcionales nulos desde ObjectMother, cuando se valida, entonces no debe tener violaciones")
    void givenProductDtoWithOptionalNullsFromObjectMother_whenValidated_thenShouldNotHaveViolations() {
        // Given - Crear un DTO con todos los campos opcionales nulos
        ProductDto dto = new ProductDto(
                "prod001",
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
        Set<ConstraintViolation<ProductDto>> violations = validator.validate(dto);

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
                Arguments.of("http://example.com/image.png#section"),
                Arguments.of("https://cdn.example.com/products/12345/image.webp"),
                Arguments.of("http://images.example.com/product_photo.jpg")
        );
    }
}