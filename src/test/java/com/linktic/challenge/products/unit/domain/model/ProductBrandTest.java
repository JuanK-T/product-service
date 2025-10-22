package com.linktic.challenge.products.unit.domain.model;

import com.linktic.challenge.products.domain.exception.valueobject.InvalidBrandException;
import com.linktic.challenge.products.domain.model.ProductBrand;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class ProductBrandTest {

    private static final int MIN_LENGTH = 2;
    private static final int MAX_LENGTH = 50;

    @Test
    @DisplayName("Dado una marca válida, cuando se crea ProductBrand, entonces debe crearse correctamente")
    void givenValidBrand_whenCreatingProductBrand_thenShouldCreateSuccessfully() {
        // Given
        String validBrand = "Samsung";

        // When
        ProductBrand brand = new ProductBrand(validBrand);

        // Then
        assertEquals(validBrand, brand.value());
    }

    @Test
    @DisplayName("Dado valor nulo, cuando se crea ProductBrand, entonces debe crearse correctamente")
    void givenNullValue_whenCreatingProductBrand_thenShouldCreateSuccessfully() {
        // When & Then
        assertDoesNotThrow(() -> {
            ProductBrand brand = new ProductBrand(null);
            assertNull(brand.value());
        });
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "Apple",
            "Sony",
            "LG Electronics",
            "HP-Inc",
            "Dell Technologies",
            "Microsoft Corp",
            "Samsung Electronics",
            "Lenovo Group",
            "Asus Tek",
            "Acer Incorporated"
    })
    @DisplayName("Dado diferentes marcas válidas, cuando se crea ProductBrand, entonces deben crearse correctamente")
    void givenDifferentValidBrands_whenCreatingProductBrand_thenShouldCreateSuccessfully(String validBrand) {
        // When & Then
        assertDoesNotThrow(() -> {
            ProductBrand brand = new ProductBrand(validBrand);
            assertEquals(validBrand, brand.value());
        });
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "   ", "\t", "\n", "\r\n"})
    @DisplayName("Dado marca vacía o en blanco, cuando se crea ProductBrand, entonces debe lanzar InvalidBrandException")
    void givenBlankBrand_whenCreatingProductBrand_thenShouldThrowInvalidBrandException(String blankBrand) {
        // When & Then
        InvalidBrandException exception = assertThrows(InvalidBrandException.class, () ->
                new ProductBrand(blankBrand)
        );

        assertEquals("Invalid brand: Brand cannot be blank if provided", exception.getMessage());
    }

    @Test
    @DisplayName("Dado marca con un solo carácter, cuando se crea ProductBrand, entonces debe lanzar InvalidBrandException")
    void givenBrandWithSingleCharacter_whenCreatingProductBrand_thenShouldThrowInvalidBrandException() {
        // Given
        String singleCharBrand = "A";

        // When & Then
        InvalidBrandException exception = assertThrows(InvalidBrandException.class, () ->
                new ProductBrand(singleCharBrand)
        );

        assertEquals("Invalid brand: Brand must be at least " + MIN_LENGTH + " characters long", exception.getMessage());
    }

    @Test
    @DisplayName("Dado marca que excede longitud máxima, cuando se crea ProductBrand, entonces debe lanzar InvalidBrandException")
    void givenBrandExceedingMaximumLength_whenCreatingProductBrand_thenShouldThrowInvalidBrandException() {
        // Given
        String longBrand = "Marca extremadamente larga que excede el límite máximo permitido para el sistema de marcas";

        // When & Then
        InvalidBrandException exception = assertThrows(InvalidBrandException.class, () ->
                new ProductBrand(longBrand)
        );

        assertEquals("Invalid brand: Brand cannot exceed " + MAX_LENGTH + " characters", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "Marca@Inválida",
            "Marca#Especial",
            "Marca$Premium",
            "Marca*Estrella",
            "Marca/División",
            "Marca\\Inversa",
            "Marca|Pipe",
            "Marca<Menor",
            "Marca>Mayor",
            "Marca{Llave",
            "Marca}Llave",
            "Marca[Corchete",
            "Marca]Corchete",
            "Marca(Paréntesis"
    })
    @DisplayName("Dado marca con caracteres inválidos, cuando se crea ProductBrand, entonces debe lanzar InvalidBrandException")
    void givenBrandWithInvalidCharacters_whenCreatingProductBrand_thenShouldThrowInvalidBrandException(String invalidBrand) {
        // When & Then
        InvalidBrandException exception = assertThrows(InvalidBrandException.class, () ->
                new ProductBrand(invalidBrand)
        );

        assertEquals("Invalid brand: Brand contains invalid characters", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "12345",
            "123 456",
            "12-34",
            "1234&5678"
    })
    @DisplayName("Dado marca que solo contiene números, cuando se crea ProductBrand, entonces debe lanzar InvalidBrandException")
    void givenBrandWithOnlyNumbers_whenCreatingProductBrand_thenShouldThrowInvalidBrandException(String numericBrand) {
        // When & Then
        InvalidBrandException exception = assertThrows(InvalidBrandException.class, () ->
                new ProductBrand(numericBrand)
        );

        assertEquals("Invalid brand: Brand must contain at least one letter", exception.getMessage());
    }

    @Test
    @DisplayName("Dado dos ProductBrand con el mismo valor, cuando se comparan, entonces deben ser iguales")
    void givenTwoProductBrandsWithSameValue_whenComparing_thenShouldBeEqual() {
        // Given
        String brandValue = "Samsung";
        ProductBrand brand1 = new ProductBrand(brandValue);
        ProductBrand brand2 = new ProductBrand(brandValue);

        // Then
        assertEquals(brand1, brand2);
        assertEquals(brand1.hashCode(), brand2.hashCode());
    }

    @Test
    @DisplayName("Dado dos ProductBrand con valores diferentes, cuando se comparan, entonces no deben ser iguales")
    void givenTwoProductBrandsWithDifferentValues_whenComparing_thenShouldNotBeEqual() {
        // Given
        ProductBrand brand1 = new ProductBrand("Samsung");
        ProductBrand brand2 = new ProductBrand("Apple");

        // Then
        assertNotEquals(brand1, brand2);
    }

    @Test
    @DisplayName("Dado dos ProductBrand nulas, cuando se comparan, entonces deben ser iguales")
    void givenTwoNullProductBrands_whenComparing_thenShouldBeEqual() {
        // Given
        ProductBrand brand1 = new ProductBrand(null);
        ProductBrand brand2 = new ProductBrand(null);

        // Then
        assertEquals(brand1, brand2);
        assertEquals(brand1.hashCode(), brand2.hashCode());
    }

    @Test
    @DisplayName("Dado un ProductBrand, cuando se obtiene su representación string, entonces debe incluir el valor")
    void givenProductBrand_whenCallingToString_thenShouldIncludeValue() {
        // Given
        String brandValue = "Samsung";
        ProductBrand brand = new ProductBrand(brandValue);

        // When
        String stringRepresentation = brand.toString();

        // Then
        assertTrue(stringRepresentation.contains(brandValue));
    }

    @Test
    @DisplayName("Dado un ProductBrand nulo, cuando se obtiene su representación string, entonces debe manejarlo correctamente")
    void givenNullProductBrand_whenCallingToString_thenShouldHandleCorrectly() {
        // Given
        ProductBrand brand = new ProductBrand(null);

        // When
        String stringRepresentation = brand.toString();

        // Then
        assertNotNull(stringRepresentation);
    }

    @Test
    @DisplayName("Dado parámetros válidos, cuando se usa factory method of(), entonces debe crear el mismo ProductBrand")
    void givenValidParameters_whenUsingFactoryMethod_thenShouldCreateSameProductBrand() {
        // Given
        String validBrand = "Samsung";

        // When
        ProductBrand fromConstructor = new ProductBrand(validBrand);
        ProductBrand fromFactory = ProductBrand.of(validBrand);

        // Then
        assertEquals(fromConstructor.value(), fromFactory.value());
        assertEquals(validBrand, fromFactory.value());
    }

    @Test
    @DisplayName("Dado valor nulo, cuando se usa factory method of(), entonces debe crear ProductBrand nulo")
    void givenNullValue_whenUsingFactoryMethod_thenShouldCreateNullProductBrand() {
        // When
        ProductBrand brand = ProductBrand.of(null);

        // Then
        assertNull(brand.value());
    }

    @ParameterizedTest
    @MethodSource("validBrandsProvider")
    @DisplayName("Dado diferentes marcas válidas con caracteres especiales permitidos, cuando se crea ProductBrand, entonces deben crearse correctamente")
    void givenValidBrandsWithAllowedSpecialChars_whenCreatingProductBrand_thenShouldCreateSuccessfully(String validBrand, String description) {
        // When & Then
        assertDoesNotThrow(() -> {
            ProductBrand brand = new ProductBrand(validBrand);
            assertEquals(validBrand, brand.value());
        }, "Should accept valid brand: " + description);
    }

    static Stream<Arguments> validBrandsProvider() {
        return Stream.of(
                Arguments.of("AB", "Minimum length"),
                Arguments.of("Samsung Electronics", "With space"),
                Arguments.of("LG-Electronics", "With hyphen"),
                Arguments.of("HP Inc.", "With dot"),
                Arguments.of("Sony & Co", "With ampersand"),
                Arguments.of("Marca Española", "With Spanish characters"),
                Arguments.of("Cámaras Nikon", "With accents"),
                Arguments.of("A".repeat(MAX_LENGTH), "Maximum length")
        );
    }

    @ParameterizedTest
    @MethodSource("invalidBrandsProvider")
    @DisplayName("Dado diferentes marcas inválidas, cuando se crea ProductBrand, entonces debe lanzar InvalidBrandException")
    void givenInvalidBrands_whenCreatingProductBrand_thenShouldThrowInvalidBrandException(String invalidBrand, String expectedError) {
        // When & Then
        InvalidBrandException exception = assertThrows(InvalidBrandException.class, () ->
                new ProductBrand(invalidBrand)
        );

        assertEquals("Invalid brand: " + expectedError, exception.getMessage());
    }

    static Stream<Arguments> invalidBrandsProvider() {
        return Stream.of(
                Arguments.of("", "Brand cannot be blank if provided"),
                Arguments.of(" ", "Brand cannot be blank if provided"),
                Arguments.of("A", "Brand must be at least " + MIN_LENGTH + " characters long"),
                Arguments.of("Marca@Inválida", "Brand contains invalid characters"),
                Arguments.of("12345", "Brand must contain at least one letter"),
                Arguments.of("12 34", "Brand must contain at least one letter"),
                Arguments.of("A".repeat(MAX_LENGTH + 1), "Brand cannot exceed " + MAX_LENGTH + " characters")
        );
    }

    @Test
    @DisplayName("Dado marcas realistas de tecnología, cuando se crea ProductBrand, entonces deben crearse correctamente")
    void givenRealisticTechBrands_whenCreatingProductBrand_thenShouldCreateSuccessfully() {
        // Given
        String[] realisticBrands = {
                "Apple",
                "Samsung",
                "Sony",
                "LG",
                "HP",
                "Dell",
                "Lenovo",
                "Asus",
                "Acer",
                "Microsoft",
                "Google",
                "Huawei",
                "Xiaomi",
                "OnePlus",
                "Nokia"
        };

        // When & Then
        for (String brand : realisticBrands) {
            assertDoesNotThrow(() -> {
                ProductBrand productBrand = new ProductBrand(brand);
                assertEquals(brand, productBrand.value());
                assertTrue(productBrand.value().length() >= MIN_LENGTH, "Brand should meet minimum length: " + brand);
                assertTrue(productBrand.value().length() <= MAX_LENGTH, "Brand should not exceed maximum length: " + brand);
            }, "Should accept realistic brand: " + brand);
        }
    }

    @Test
    @DisplayName("Dado comparación con null y otro tipo, cuando se verifica equals, entonces debe retornar false")
    void givenNullAndDifferentType_whenCheckingEquals_thenShouldReturnFalse() {
        // Given
        ProductBrand brand = new ProductBrand("Samsung");

        // Then
        assertNotEquals(null, brand, "Equals con null debe retornar false");
    }

    @Test
    @DisplayName("Dado marca con números y letras, cuando se crea ProductBrand, entonces debe crearse correctamente")
    void givenBrandWithNumbersAndLetters_whenCreatingProductBrand_thenShouldCreateSuccessfully() {
        // Given
        String[] mixedBrands = {
                "iPhone 14",
                "Galaxy S23",
                "XPS 13",
                "ThinkPad X1",
                "Surface Pro 9",
                "MacBook Pro 16",
                "Yoga 9i",
                "ZenBook 14",
                "Swift 3",
                "Inspiron 15"
        };

        // When & Then
        for (String brand : mixedBrands) {
            assertDoesNotThrow(() -> {
                ProductBrand productBrand = new ProductBrand(brand);
                assertEquals(brand, productBrand.value());
            }, "Should accept mixed brand: " + brand);
        }
    }

    @Test
    @DisplayName("Dado marcas con diferentes casos, cuando se crea ProductBrand, entonces deben preservar el caso original")
    void givenBrandsWithDifferentCases_whenCreatingProductBrand_thenShouldPreserveOriginalCase() {
        // Given
        String lowercase = "samsung";
        String uppercase = "SAMSUNG";
        String mixedCase = "Samsung";

        // When
        ProductBrand lower = new ProductBrand(lowercase);
        ProductBrand upper = new ProductBrand(uppercase);
        ProductBrand mixed = new ProductBrand(mixedCase);

        // Then
        assertEquals(lowercase, lower.value());
        assertEquals(uppercase, upper.value());
        assertEquals(mixedCase, mixed.value());
        assertNotEquals(lower, upper);
        assertNotEquals(lower, mixed);
    }

    @Test
    @DisplayName("Dado marcas con nombres corporativos completos, cuando se crea ProductBrand, entonces deben crearse correctamente")
    void givenCorporateBrandNames_whenCreatingProductBrand_thenShouldCreateSuccessfully() {
        // Given
        String[] corporateBrands = {
                "Samsung Electronics Co., Ltd.",
                "LG Electronics Inc.",
                "Sony Corporation",
                "Dell Technologies Inc.",
                "HP Inc. Company"
        };

        // When & Then
        for (String brand : corporateBrands) {
            assertDoesNotThrow(() -> {
                ProductBrand productBrand = new ProductBrand(brand);
                assertEquals(brand, productBrand.value());
            }, "Should accept corporate brand: " + brand);
        }
    }
}
