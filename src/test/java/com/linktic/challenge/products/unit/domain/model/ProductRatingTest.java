package com.linktic.challenge.products.unit.domain.model;

import com.linktic.challenge.products.domain.exception.valueobject.InvalidRatingException;
import com.linktic.challenge.products.domain.model.ProductRating;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.NullSource;

import static org.junit.jupiter.api.Assertions.*;

class ProductRatingTest {

    @Test
    @DisplayName("Dado un valor válido entre 0 y 5, cuando se crea ProductRating, entonces no debe lanzar excepción")
    void givenValidValue_whenCreatingProductRating_thenShouldNotThrowException() {
        // Given
        Double validValue = 4.5;

        // When & Then
        assertDoesNotThrow(() -> {
            ProductRating rating = new ProductRating(validValue);
            assertEquals(validValue, rating.value());
        });
    }

    @Test
    @DisplayName("Dado el valor mínimo (0.0), cuando se crea ProductRating, entonces debe crearse correctamente")
    void givenMinimumValue_whenCreatingProductRating_thenShouldCreateSuccessfully() {
        // Given
        Double minValue = 0.0;

        // When
        ProductRating rating = new ProductRating(minValue);

        // Then
        assertEquals(minValue, rating.value());
    }

    @Test
    @DisplayName("Dado el valor máximo (5.0), cuando se crea ProductRating, entonces debe crearse correctamente")
    void givenMaximumValue_whenCreatingProductRating_thenShouldCreateSuccessfully() {
        // Given
        Double maxValue = 5.0;

        // When
        ProductRating rating = new ProductRating(maxValue);

        // Then
        assertEquals(maxValue, rating.value());
    }

    @Test
    @DisplayName("Dado valores límite válidos, cuando se crea ProductRating, entonces deben crearse correctamente")
    void givenBoundaryValidValues_whenCreatingProductRating_thenShouldCreateSuccessfully() {
        // Given
        Double[] validValues = {0.0, 0.1, 2.5, 4.9, 5.0};

        // When & Then
        for (Double value : validValues) {
            assertDoesNotThrow(() -> {
                ProductRating rating = new ProductRating(value);
                assertEquals(value, rating.value());
            }, "Should not throw for valid value: " + value);
        }
    }

    @ParameterizedTest
    @NullSource
    @DisplayName("Dado un valor nulo, cuando se crea ProductRating, entonces debe lanzar InvalidRatingException")
    void givenNullValue_whenCreatingProductRating_thenShouldThrowInvalidRatingException(Double nullValue) {
        // When & Then
        InvalidRatingException exception = assertThrows(InvalidRatingException.class, () -> new ProductRating(nullValue));

        assertEquals("Invalid rating: Cannot be null", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(doubles = {-0.1, -1.0, -5.0, -100.0, 5.1, 6.0, 10.0, 100.0})
    @DisplayName("Dado valores fuera del rango [0,5], cuando se crea ProductRating, entonces debe lanzar InvalidRatingException")
    void givenValuesOutsideValidRange_whenCreatingProductRating_thenShouldThrowInvalidRatingException(Double invalidValue) {
        // When & Then
        InvalidRatingException exception = assertThrows(InvalidRatingException.class, () ->
                new ProductRating(invalidValue)
        );

        assertEquals("Invalid rating: Must be between 0 and 5 (inclusive)", exception.getMessage());
    }

    @Test
    @DisplayName("Dado el valor NaN, cuando se crea ProductRating, entonces debe lanzar InvalidRatingException")
    void givenNaNValue_whenCreatingProductRating_thenShouldThrowInvalidRatingException() {
        // Given
        Double nanValue = Double.NaN;

        // When & Then
        InvalidRatingException exception = assertThrows(InvalidRatingException.class, () -> new ProductRating(nanValue));

        assertEquals("Invalid rating: Must be a finite number", exception.getMessage());
    }

    @Test
    @DisplayName("Dado el valor infinito positivo, cuando se crea ProductRating, entonces debe lanzar InvalidRatingException")
    void givenPositiveInfiniteValue_whenCreatingProductRating_thenShouldThrowInvalidRatingException() {
        // Given
        Double infiniteValue = Double.POSITIVE_INFINITY;

        // When & Then
        InvalidRatingException exception = assertThrows(InvalidRatingException.class, () -> new ProductRating(infiniteValue));

        assertEquals("Invalid rating: Must be a finite number", exception.getMessage());
    }

    @Test
    @DisplayName("Dado el valor infinito negativo, cuando se crea ProductRating, entonces debe lanzar InvalidRatingException")
    void givenNegativeInfiniteValue_whenCreatingProductRating_thenShouldThrowInvalidRatingException() {
        // Given
        Double infiniteValue = Double.NEGATIVE_INFINITY;

        // When & Then
        InvalidRatingException exception = assertThrows(InvalidRatingException.class, () -> new ProductRating(infiniteValue));

        assertEquals("Invalid rating: Must be a finite number", exception.getMessage());
    }

    @Test
    @DisplayName("Dado un valor válido, cuando se usa el método factory of(), entonces debe crear el mismo ProductRating")
    void givenValidValue_whenUsingFactoryMethod_thenShouldCreateSameProductRating() {
        // Given
        Double validValue = 3.8;

        // When
        ProductRating fromConstructor = new ProductRating(validValue);
        ProductRating fromFactory = ProductRating.of(validValue);

        // Then
        assertEquals(fromConstructor.value(), fromFactory.value());
        assertEquals(validValue, fromFactory.value());
    }

    @Test
    @DisplayName("Dado un valor inválido, cuando se usa el método factory of(), entonces debe lanzar la misma excepción")
    void givenInvalidValue_whenUsingFactoryMethod_thenShouldThrowSameException() {
        // Given
        Double invalidValue = -2.0;

        // When & Then
        InvalidRatingException exception = assertThrows(InvalidRatingException.class, () -> ProductRating.of(invalidValue));

        assertEquals("Invalid rating: Must be between 0 and 5 (inclusive)", exception.getMessage());
    }

    @Test
    @DisplayName("Dado dos ProductRating con el mismo valor, cuando se comparan, entonces deben ser iguales")
    void givenTwoProductRatingsWithSameValue_whenComparing_thenShouldBeEqual() {
        // Given
        Double value = 4.2;
        ProductRating rating1 = new ProductRating(value);
        ProductRating rating2 = new ProductRating(value);

        // Then
        assertEquals(rating1, rating2);
        assertEquals(rating1.hashCode(), rating2.hashCode());
    }

    @Test
    @DisplayName("Dado dos ProductRating con valores diferentes, cuando se comparan, entonces no deben ser iguales")
    void givenTwoProductRatingsWithDifferentValues_whenComparing_thenShouldNotBeEqual() {
        // Given
        ProductRating rating1 = new ProductRating(3.5);
        ProductRating rating2 = new ProductRating(4.5);

        // Then
        assertNotEquals(rating1, rating2);
    }

    @Test
    @DisplayName("Dado un ProductRating, cuando se obtiene su representación string, entonces debe incluir el valor")
    void givenProductRating_whenCallingToString_thenShouldIncludeValue() {
        // Given
        Double value = 4.7;
        ProductRating rating = new ProductRating(value);

        // When
        String stringRepresentation = rating.toString();

        // Then
        assertTrue(stringRepresentation.contains(value.toString()));
    }

    @ParameterizedTest
    @ValueSource(doubles = {0.0, 2.5, 5.0})
    @DisplayName("Dado valores válidos en los límites, cuando se crea ProductRating mediante constructor y factory, entonces deben ser equivalentes")
    void givenValidBoundaryValues_whenUsingConstructorAndFactory_thenShouldBeEquivalent(Double value) {
        // When
        ProductRating fromConstructor = new ProductRating(value);
        ProductRating fromFactory = ProductRating.of(value);

        // Then
        assertEquals(fromConstructor, fromFactory);
        assertEquals(fromConstructor.value(), fromFactory.value());
    }
}
