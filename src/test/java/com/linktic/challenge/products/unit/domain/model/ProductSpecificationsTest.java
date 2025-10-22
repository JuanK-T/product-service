package com.linktic.challenge.products.unit.domain.model;

import com.linktic.challenge.products.domain.exception.valueobject.InvalidSpecificationsException;
import com.linktic.challenge.products.domain.model.ProductSpecifications;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ProductSpecificationsTest {

    // Tests para casos válidos
    @Test
    @DisplayName("Dado un mapa de especificaciones válido, cuando se crea ProductSpecifications, entonces debe crearse correctamente")
    void givenValidSpecificationsMap_whenCreatingProductSpecifications_thenShouldCreateSuccessfully() {
        // Given
        Map<String, String> validSpecs = Map.of(
                "pantalla", "6.5 pulgadas AMOLED",
                "procesador", "Snapdragon 8 Gen 2",
                "memoria", "256GB"
        );

        // When
        ProductSpecifications productSpecs = new ProductSpecifications(validSpecs);

        // Then
        assertNotNull(productSpecs);
        assertEquals(validSpecs, productSpecs.specs());
    }

    @Test
    @DisplayName("Dado un mapa nulo, cuando se crea ProductSpecifications, entonces debe crear un mapa vacío")
    void givenNullMap_whenCreatingProductSpecifications_thenShouldCreateEmptyMap() {
        // When
        ProductSpecifications productSpecs = new ProductSpecifications(null);

        // Then
        assertNotNull(productSpecs);
        assertTrue(productSpecs.specs().isEmpty());
    }

    @Test
    @DisplayName("Dado un mapa vacío, cuando se crea ProductSpecifications, entonces debe crearse correctamente")
    void givenEmptyMap_whenCreatingProductSpecifications_thenShouldCreateSuccessfully() {
        // Given
        Map<String, String> emptySpecs = Map.of();

        // When
        ProductSpecifications productSpecs = new ProductSpecifications(emptySpecs);

        // Then
        assertNotNull(productSpecs);
        assertTrue(productSpecs.specs().isEmpty());
    }

    @Test
    @DisplayName("Dado el máximo número de especificaciones permitidas (10), cuando se crea ProductSpecifications, entonces debe crearse correctamente")
    void givenMaximumAllowedSpecifications_whenCreatingProductSpecifications_thenShouldCreateSuccessfully() {
        // Given
        Map<String, String> maxSpecs = new HashMap<>();
        for (int i = 1; i <= 10; i++) {
            maxSpecs.put("key" + i, "value" + i);
        }

        // When
        ProductSpecifications productSpecs = new ProductSpecifications(maxSpecs);

        // Then
        assertNotNull(productSpecs);
        assertEquals(10, productSpecs.specs().size());
    }

    @Test
    @DisplayName("Dado especificaciones con longitud máxima permitida, cuando se crea ProductSpecifications, entonces debe crearse correctamente")
    void givenMaxLengthKeysAndValues_whenCreatingProductSpecifications_thenShouldCreateSuccessfully() {
        // Given
        String maxLengthKey = "a".repeat(50);
        String maxLengthValue = "b".repeat(80);
        Map<String, String> specs = Map.of(maxLengthKey, maxLengthValue);

        // When
        ProductSpecifications productSpecs = new ProductSpecifications(specs);

        // Then
        assertNotNull(productSpecs);
        assertEquals(maxLengthValue, productSpecs.specs().get(maxLengthKey));
    }

    // Tests para casos inválidos - excepciones

    @Test
    @DisplayName("Dado un mapa con clave null, cuando se crea ProductSpecifications, entonces debe lanzar InvalidSpecificationsException")
    void givenMapWithNullKey_whenCreatingProductSpecifications_thenShouldThrowException() {
        // Given
        Map<String, String> specsWithNullKey = new HashMap<>();
        specsWithNullKey.put(null, "valor válido");
        specsWithNullKey.put("keyNormal", "valor normal");

        // When & Then
        InvalidSpecificationsException exception = assertThrows(
                InvalidSpecificationsException.class,
                () -> new ProductSpecifications(specsWithNullKey)
        );

        assertEquals("Invalid specifications: Specification key or value cannot be null",
                exception.getMessage());
    }

    @Test
    @DisplayName("Dado un mapa con valor null, cuando se crea ProductSpecifications, entonces debe lanzar InvalidSpecificationsException")
    void givenMapWithNullValue_whenCreatingProductSpecifications_thenShouldThrowException() {
        // Given
        Map<String, String> specsWithNullValue = new HashMap<>();
        specsWithNullValue.put("keyNormal", "valor normal");
        specsWithNullValue.put("keyNull", null);

        // When & Then
        InvalidSpecificationsException exception = assertThrows(
                InvalidSpecificationsException.class,
                () -> new ProductSpecifications(specsWithNullValue)
        );

        assertEquals("Invalid specifications: Specification key or value cannot be null",
                exception.getMessage());
    }

    @Test
    @DisplayName("Dado un mapa con clave vacía, cuando se crea ProductSpecifications, entonces debe lanzar InvalidSpecificationsException")
    void givenMapWithEmptyKey_whenCreatingProductSpecifications_thenShouldThrowException() {
        // Given
        Map<String, String> specsWithEmptyKey = Map.of("", "valor válido");

        // When & Then
        InvalidSpecificationsException exception = assertThrows(
                InvalidSpecificationsException.class,
                () -> new ProductSpecifications(specsWithEmptyKey)
        );

        assertEquals("Invalid specifications: Specification key or value cannot be empty",
                exception.getMessage());
    }

    @Test
    @DisplayName("Dado un mapa con clave que solo tiene espacios, cuando se crea ProductSpecifications, entonces debe lanzar InvalidSpecificationsException")
    void givenMapWithBlankKey_whenCreatingProductSpecifications_thenShouldThrowException() {
        // Given
        Map<String, String> specsWithBlankKey = Map.of("   ", "valor válido");

        // When & Then
        InvalidSpecificationsException exception = assertThrows(
                InvalidSpecificationsException.class,
                () -> new ProductSpecifications(specsWithBlankKey)
        );

        assertEquals("Invalid specifications: Specification key or value cannot be empty",
                exception.getMessage());
    }

    @Test
    @DisplayName("Dado un mapa con valor vacío, cuando se crea ProductSpecifications, entonces debe lanzar InvalidSpecificationsException")
    void givenMapWithEmptyValue_whenCreatingProductSpecifications_thenShouldThrowException() {
        // Given
        Map<String, String> specsWithEmptyValue = Map.of("claveVálida", "");

        // When & Then
        InvalidSpecificationsException exception = assertThrows(
                InvalidSpecificationsException.class,
                () -> new ProductSpecifications(specsWithEmptyValue)
        );

        assertEquals("Invalid specifications: Specification key or value cannot be empty",
                exception.getMessage());
    }

    @Test
    @DisplayName("Dado un mapa con valor que solo tiene espacios, cuando se crea ProductSpecifications, entonces debe lanzar InvalidSpecificationsException")
    void givenMapWithBlankValue_whenCreatingProductSpecifications_thenShouldThrowException() {
        // Given
        Map<String, String> specsWithBlankValue = Map.of("claveVálida", "   ");

        // When & Then
        InvalidSpecificationsException exception = assertThrows(
                InvalidSpecificationsException.class,
                () -> new ProductSpecifications(specsWithBlankValue)
        );

        assertEquals("Invalid specifications: Specification key or value cannot be empty",
                exception.getMessage());
    }

    @Test
    @DisplayName("Dado un mapa con clave demasiado larga, cuando se crea ProductSpecifications, entonces debe lanzar InvalidSpecificationsException")
    void givenMapWithKeyTooLong_whenCreatingProductSpecifications_thenShouldThrowException() {
        // Given
        String longKey = "a".repeat(51);
        Map<String, String> specsWithLongKey = Map.of(longKey, "valor válido");

        // When & Then
        InvalidSpecificationsException exception = assertThrows(
                InvalidSpecificationsException.class,
                () -> new ProductSpecifications(specsWithLongKey)
        );

        assertEquals("Invalid specifications: Specification key too long (max 50): " + longKey,
                exception.getMessage());
    }

    @Test
    @DisplayName("Dado un mapa con valor demasiado largo, cuando se crea ProductSpecifications, entonces debe lanzar InvalidSpecificationsException")
    void givenMapWithValueTooLong_whenCreatingProductSpecifications_thenShouldThrowException() {
        // Given
        String longValue = "b".repeat(81);
        Map<String, String> specsWithLongValue = Map.of("claveVálida", longValue);

        // When & Then
        InvalidSpecificationsException exception = assertThrows(
                InvalidSpecificationsException.class,
                () -> new ProductSpecifications(specsWithLongValue)
        );

        assertEquals("Invalid specifications: Specification value too long (max 80) for key: claveVálida",
                exception.getMessage());
    }

    @Test
    @DisplayName("Dado un mapa con más de 10 especificaciones, cuando se crea ProductSpecifications, entonces debe lanzar InvalidSpecificationsException")
    void givenMapWithTooManySpecifications_whenCreatingProductSpecifications_thenShouldThrowException() {
        // Given
        Map<String, String> oversizedSpecs = new HashMap<>();
        for (int i = 1; i <= 11; i++) {
            oversizedSpecs.put("key" + i, "value" + i);
        }

        // When & Then
        InvalidSpecificationsException exception = assertThrows(
                InvalidSpecificationsException.class,
                () -> new ProductSpecifications(oversizedSpecs)
        );

        assertEquals("Invalid specifications: Maximum 10 specifications exceeded",
                exception.getMessage());
    }

    @Test
    @DisplayName("Dado múltiples problemas en especificaciones, cuando se crea ProductSpecifications, entonces debe lanzar la primera excepción encontrada")
    void givenMultipleValidationIssues_whenCreatingProductSpecifications_thenShouldThrowFirstExceptionFound() {
        // Given
        Map<String, String> invalidSpecs = new HashMap<>();
        invalidSpecs.put("", "valor válido"); // clave vacía
        invalidSpecs.put("claveVálida", null); // valor null

        // When & Then
        InvalidSpecificationsException exception = assertThrows(
                InvalidSpecificationsException.class,
                () -> new ProductSpecifications(invalidSpecs)
        );

        // Debe lanzar la primera excepción que encuentre (puede variar según el orden del stream)
        assertTrue(exception.getMessage().contains("Invalid specifications:"));
    }

    @Test
    @DisplayName("Dado especificaciones con espacios alrededor, cuando se crea ProductSpecifications, entonces debe preservar los espacios internos")
    void givenSpecificationsWithSurroundingSpaces_whenCreatingProductSpecifications_thenShouldPreserveInternalSpaces() {
        // Given
        Map<String, String> specs = Map.of(
                "  claveConEspacios  ", "  valor con espacios  ",
                "claveNormal", "valor normal"
        );

        // When
        ProductSpecifications productSpecs = new ProductSpecifications(specs);

        // Then - Los espacios internos se preservan, los del alrededor se eliminan por el trim()
        assertEquals("valor con espacios", productSpecs.specs().get("claveConEspacios"));
        assertEquals("valor normal", productSpecs.specs().get("claveNormal"));
    }

    @Test
    @DisplayName("Dado el mismo mapa de especificaciones, cuando se crean dos ProductSpecifications, entonces deben ser iguales")
    void givenSameSpecificationsMap_whenCreatingTwoProductSpecifications_thenShouldBeEqual() {
        // Given
        Map<String, String> specs1 = Map.of("pantalla", "6.5 pulgadas");
        Map<String, String> specs2 = Map.of("pantalla", "6.5 pulgadas");

        // When
        ProductSpecifications productSpecs1 = new ProductSpecifications(specs1);
        ProductSpecifications productSpecs2 = new ProductSpecifications(specs2);

        // Then
        assertEquals(productSpecs1, productSpecs2);
        assertEquals(productSpecs1.hashCode(), productSpecs2.hashCode());
    }

    @Test
    @DisplayName("Dado diferentes mapas de especificaciones, cuando se crean dos ProductSpecifications, entonces deben ser diferentes")
    void givenDifferentSpecificationsMaps_whenCreatingTwoProductSpecifications_thenShouldNotBeEqual() {
        // Given
        Map<String, String> specs1 = Map.of("pantalla", "6.5 pulgadas");
        Map<String, String> specs2 = Map.of("procesador", "Snapdragon 8 Gen 2");

        // When
        ProductSpecifications productSpecs1 = new ProductSpecifications(specs1);
        ProductSpecifications productSpecs2 = new ProductSpecifications(specs2);

        // Then
        assertNotEquals(productSpecs1, productSpecs2);
    }

    @Test
    @DisplayName("Dado ProductSpecifications, cuando se intenta modificar el mapa retornado, entonces debe lanzar excepción")
    void givenProductSpecifications_whenModifyingReturnedMap_thenShouldThrowException() {
        // Given
        Map<String, String> specs = Map.of("pantalla", "6.5 pulgadas");
        ProductSpecifications productSpecs = new ProductSpecifications(specs);
        Map<String, String> returnedSpecs = productSpecs.specs();

        // When & Then
        assertThrows(UnsupportedOperationException.class,
                () -> returnedSpecs.put("nueva", "especificación"));
    }

    @Test
    @DisplayName("Dado especificaciones inmutables, cuando se intenta modificar el mapa original, entonces no debe afectar las especificaciones")
    void givenImmutableSpecifications_whenModifyingOriginalMap_thenShouldNotAffectSpecifications() {
        // Given
        Map<String, String> originalSpecs = new HashMap<>();
        originalSpecs.put("pantalla", "6.5 pulgadas");
        ProductSpecifications productSpecs = new ProductSpecifications(originalSpecs);

        // When
        originalSpecs.put("nueva", "especificación"); // Modificar el mapa original

        // Then
        assertEquals(1, productSpecs.specs().size()); // Las especificaciones no cambian
        assertFalse(productSpecs.specs().containsKey("nueva"));
    }

    @Test
    @DisplayName("Dado especificaciones con diferentes casos, cuando se crea ProductSpecifications, entonces debe ser case sensitive")
    void givenSpecificationsWithDifferentCases_whenCreatingProductSpecifications_thenShouldBeCaseSensitive() {
        // Given
        Map<String, String> specs = Map.of(
                "Pantalla", "6.5 pulgadas",
                "pantalla", "7.0 pulgadas"
        );

        // When
        ProductSpecifications productSpecs = new ProductSpecifications(specs);

        // Then
        assertEquals(2, productSpecs.specs().size());
        assertEquals("6.5 pulgadas", productSpecs.specs().get("Pantalla"));
        assertEquals("7.0 pulgadas", productSpecs.specs().get("pantalla"));
    }
}