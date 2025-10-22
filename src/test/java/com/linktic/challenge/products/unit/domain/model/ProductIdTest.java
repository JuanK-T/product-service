package com.linktic.challenge.products.unit.domain.model;

import com.linktic.challenge.products.domain.exception.valueobject.InvalidProductIdException;
import com.linktic.challenge.products.domain.model.ProductId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class ProductIdTest {

    @ParameterizedTest
    @ValueSource(strings = {
            "prod123",
            "PROD456",
            "prod_123",
            "prod-456",
            "123ABC",
            "p"
    })
    @DisplayName("Dado IDs válidos con caracteres alfanuméricos, cuando se crea ProductId, entonces debe crearse correctamente")
    void givenValidAlphanumericIds_whenCreatingProductId_thenShouldCreateSuccessfully(String validId) {
        // When & Then
        assertDoesNotThrow(() -> {
            ProductId productId = new ProductId(validId);
            assertEquals(validId, productId.value());
        });
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "prod-001",      // ID con guiones
            "prod_001",      // ID con guiones bajos
            "12345",         // ID con números
            "PROD001",       // ID en mayúsculas
            "Prod_001-ABC"   // ID mixto
    })
    @DisplayName("Dado diferentes tipos de IDs válidos, cuando se crea ProductId, entonces debe crearse correctamente")
    void givenDifferentValidIdTypes_whenCreatingProductId_thenShouldCreateSuccessfully(String validId) {
        // When & Then
        assertDoesNotThrow(() -> {
            ProductId productId = new ProductId(validId);
            assertEquals(validId, productId.value());
        });
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("Dado ID nulo o vacío, cuando se crea ProductId, entonces debe lanzar InvalidProductIdException")
    void givenNullOrEmptyId_whenCreatingProductId_thenShouldThrowInvalidProductIdException(String invalidId) {
        // When & Then
        InvalidProductIdException exception = assertThrows(InvalidProductIdException.class, () ->
                new ProductId(invalidId)
        );

        if (invalidId == null) {
            assertEquals("Product id cannot be null", exception.getMessage());
        } else {
            assertEquals("Product id cannot be blank", exception.getMessage());
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {" ", "   ", "\t", "\n", "\r\n"})
    @DisplayName("Dado ID con solo espacios en blanco, cuando se crea ProductId, entonces debe lanzar InvalidProductIdException")
    void givenBlankId_whenCreatingProductId_thenShouldThrowInvalidProductIdException(String blankId) {
        // When & Then
        InvalidProductIdException exception = assertThrows(InvalidProductIdException.class, () ->
                new ProductId(blankId)
        );

        assertEquals("Product id cannot be blank", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "prod@123",        // Carácter @ no permitido
            "prod#001",        // Carácter # no permitido
            "prod 001",        // Espacio no permitido
            "prod.001",        // Punto no permitido
            "prod/001",        // Slash no permitido
            "prod?001",        // Signo de pregunta no permitido
            "prod&001",        // Ampersand no permitido
            "prod+001",        // Signo más no permitido
            "prod=001",        // Signo igual no permitido
            "prod(001)",       // Paréntesis no permitidos
            "prod[001]",       // Corchetes no permitidos
            "prod{001}",       // Llaves no permitidas
            "prod$001",        // Signo dólar no permitido
            "prod%001",        // Porcentaje no permitido
            "prod*001",        // Asterisco no permitido
            "prod¡001",        // Carácter especial español
            "prodñ001",        // Ñ no permitida
            "prodá001",        // Acento no permitido
            "prod🎮001"        // Emoji no permitido
    })
    @DisplayName("Dado ID con caracteres inválidos, cuando se crea ProductId, entonces debe lanzar InvalidProductIdException")
    void givenIdWithInvalidCharacters_whenCreatingProductId_thenShouldThrowInvalidProductIdException(String invalidId) {
        // When & Then
        InvalidProductIdException exception = assertThrows(InvalidProductIdException.class, () ->
                new ProductId(invalidId)
        );

        assertEquals("Product id contains invalid characters", exception.getMessage());
    }

    @Test
    @DisplayName("Dado dos ProductId con el mismo valor, cuando se comparan, entonces deben ser iguales")
    void givenTwoProductIdsWithSameValue_whenComparing_thenShouldBeEqual() {
        // Given
        String id = "prod001";
        ProductId productId1 = new ProductId(id);
        ProductId productId2 = new ProductId(id);

        // Then
        assertEquals(productId1, productId2);
        assertEquals(productId1.hashCode(), productId2.hashCode());
    }

    @Test
    @DisplayName("Dado dos ProductId con valores diferentes, cuando se comparan, entonces no deben ser iguales")
    void givenTwoProductIdsWithDifferentValues_whenComparing_thenShouldNotBeEqual() {
        // Given
        ProductId productId1 = new ProductId("prod001");
        ProductId productId2 = new ProductId("prod002");

        // Then
        assertNotEquals(productId1, productId2);
    }

    @Test
    @DisplayName("Dado un ProductId, cuando se obtiene su representación string, entonces debe incluir el valor")
    void givenProductId_whenCallingToString_thenShouldIncludeValue() {
        // Given
        String id = "prod001";
        ProductId productId = new ProductId(id);

        // When
        String stringRepresentation = productId.toString();

        // Then
        assertTrue(stringRepresentation.contains(id));
    }

    @ParameterizedTest
    @MethodSource("validIdProvider")
    @DisplayName("Dado diferentes IDs válidos, cuando se crea ProductId, entonces deben crearse correctamente")
    void givenDifferentValidIds_whenCreatingProductId_thenShouldCreateSuccessfully(String validId, String description) {
        // When & Then
        assertDoesNotThrow(() -> {
            ProductId productId = new ProductId(validId);
            assertEquals(validId, productId.value());
        }, "Should accept valid ID: " + description);
    }

    static Stream<Arguments> validIdProvider() {
        return Stream.of(
                Arguments.of("a", "Single lowercase letter"),
                Arguments.of("A", "Single uppercase letter"),
                Arguments.of("1", "Single digit"),
                Arguments.of("a1", "Letter and digit"),
                Arguments.of("A1", "Uppercase letter and digit"),
                Arguments.of("prod-001", "Lowercase with hyphen"),
                Arguments.of("PROD-001", "Uppercase with hyphen"),
                Arguments.of("prod_001", "Lowercase with underscore"),
                Arguments.of("PROD_001", "Uppercase with underscore"),
                Arguments.of("prod-001_ABC", "Mixed case with hyphen and underscore"),
                Arguments.of("123-456_789", "Numbers with hyphen and underscore"),
                Arguments.of("p-r-o-d_001", "Multiple hyphens and underscore"),
                Arguments.of("prod001", "Simple alphanumeric"),
                Arguments.of("PROD001", "Uppercase alphanumeric"),
                Arguments.of("Prod001", "Mixed case alphanumeric")
        );
    }

    @ParameterizedTest
    @MethodSource("invalidIdProvider")
    @DisplayName("Dado diferentes IDs inválidos, cuando se crea ProductId, entonces debe lanzar InvalidProductIdException")
    void givenDifferentInvalidIds_whenCreatingProductId_thenShouldThrowInvalidProductIdException(String invalidId, String description) {
        // When & Then
        InvalidProductIdException exception = assertThrows(InvalidProductIdException.class, () ->
                        new ProductId(invalidId),
                "Should reject invalid ID: " + description
        );

        assertTrue(exception.getMessage().contains("Product id"));
    }

    static Stream<Arguments> invalidIdProvider() {
        return Stream.of(
                Arguments.of("", "Empty string"),
                Arguments.of(null, "Null"),
                Arguments.of(" ", "Single space"),
                Arguments.of("  ", "Multiple spaces"),
                Arguments.of("\t", "Tab"),
                Arguments.of("\n", "Newline"),
                Arguments.of("prod 001", "Space in middle"),
                Arguments.of(" prod001", "Leading space"),
                Arguments.of("prod001 ", "Trailing space"),
                Arguments.of("prod@001", "At symbol"),
                Arguments.of("prod#001", "Hash symbol"),
                Arguments.of("prod.001", "Dot"),
                Arguments.of("prod/001", "Slash"),
                Arguments.of("prod\\001", "Backslash"),
                Arguments.of("prod(001)", "Parentheses"),
                Arguments.of("prod[001]", "Brackets"),
                Arguments.of("prod{001}", "Braces"),
                Arguments.of("prod$001", "Dollar sign"),
                Arguments.of("prod%001", "Percent sign"),
                Arguments.of("prod*001", "Asterisk"),
                Arguments.of("prod+001", "Plus sign"),
                Arguments.of("prod=001", "Equals sign"),
                Arguments.of("prodñ001", "Spanish ñ"),
                Arguments.of("prodá001", "Accented a"),
                Arguments.of("prod🎮001", "Emoji")
        );
    }

    @Test
    @DisplayName("Dado IDs de productos realistas, cuando se crea ProductId, entonces deben crearse correctamente")
    void givenRealisticProductIds_whenCreatingProductId_thenShouldCreateSuccessfully() {
        // Given
        String[] realisticIds = {
                "prod001",
                "PROD_2024_001",
                "smartphone-galaxy-s23",
                "laptop-pro-ultra",
                "tablet-12-max",
                "headphones-sony-xm4",
                "camera-canon-r5",
                "watch-apple-ultra",
                "keyboard-mechanical",
                "mouse-gaming-pro"
        };

        // When & Then
        for (String id : realisticIds) {
            assertDoesNotThrow(() -> {
                ProductId productId = new ProductId(id);
                assertEquals(id, productId.value());
            }, "Should accept realistic ID: " + id);
        }
    }

    @Test
    @DisplayName("Dado IDs con diferentes longitudes, cuando se crea ProductId, entonces deben crearse correctamente")
    void givenIdsWithDifferentLengths_whenCreatingProductId_thenShouldCreateSuccessfully() {
        // Given
        String shortId = "a";
        String mediumId = "prod-001";
        String longId = "very-long-product-id-with-multiple-words-2024";

        // When & Then
        assertDoesNotThrow(() -> new ProductId(shortId));
        assertDoesNotThrow(() -> new ProductId(mediumId));
        assertDoesNotThrow(() -> new ProductId(longId));
    }

    @Test
    @DisplayName("Dado comparación con null, cuando se verifica equals, entonces debe retornar false")
    void givenNull_whenCheckingEquals_thenShouldReturnFalse() {
        // Given
        ProductId productId = new ProductId("prod001");

        // Then
        assertNotEquals(null, productId, "Equals con null debe retornar false");
    }

    @Test
    @DisplayName("Dado IDs con patrones específicos, cuando se crea ProductId, entonces deben validar el regex correctamente")
    void givenIdsWithSpecificPatterns_whenCreatingProductId_thenShouldValidateRegexCorrectly() {
        // Given
        String[] validPatterns = {
                "ABC123",
                "abc-123",
                "ABC_123",
                "123-ABC",
                "123_ABC",
                "A1-B2-C3",
                "A1_B2_C3"
        };

        String[] invalidPatterns = {
                "ABC 123",      // Space
                "ABC.123",      // Dot
                "ABC@123",      // At symbol
                "ABC#123",      // Hash
                "ABC/123",      // Slash
                "ABC(123)",     // Parentheses
                "ABC[123]"      // Brackets
        };

        // When & Then for valid patterns
        for (String pattern : validPatterns) {
            assertDoesNotThrow(() -> {
                ProductId productId = new ProductId(pattern);
                assertEquals(pattern, productId.value());
            }, "Should accept valid pattern: " + pattern);
        }

        // When & Then for invalid patterns
        for (String pattern : invalidPatterns) {
            assertThrows(InvalidProductIdException.class, () ->
                            new ProductId(pattern),
                    "Should reject invalid pattern: " + pattern
            );
        }
    }
}