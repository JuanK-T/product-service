package com.linktic.challenge.products.unit.domain.exception.valueobject;

import com.linktic.challenge.products.domain.exception.valueobject.InvalidBrandException;
import com.linktic.challenge.products.domain.exception.valueobject.ProductValueObjectException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InvalidBrandExceptionTest {

    @Test
    @DisplayName("Dado una marca inválida, cuando se crea InvalidBrandException, entonces la excepción debe contener el mensaje correcto con la marca")
    void givenInvalidBrand_whenCreatingException_thenShouldContainCorrectMessageWithBrand() {
        // Given
        String brand = "InvalidBrand123";

        // When
        InvalidBrandException exception = new InvalidBrandException(brand);

        // Then
        assertEquals("Invalid brand: " + brand, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    @DisplayName("Dado diferentes marcas inválidas, cuando se crea InvalidBrandException, entonces cada excepción debe contener su respectiva marca en el mensaje")
    void givenDifferentInvalidBrands_whenCreatingExceptions_thenEachShouldContainRespectiveBrandInMessage() {
        // Given
        String brand1 = "Brand@With#Special$Chars";
        String brand2 = "TooLongBrandNameThatExceedsMaximumAllowedLength";
        String brand3 = "123NumericBrand";

        // When
        InvalidBrandException exception1 = new InvalidBrandException(brand1);
        InvalidBrandException exception2 = new InvalidBrandException(brand2);
        InvalidBrandException exception3 = new InvalidBrandException(brand3);

        // Then
        assertEquals("Invalid brand: " + brand1, exception1.getMessage());
        assertEquals("Invalid brand: " + brand2, exception2.getMessage());
        assertEquals("Invalid brand: " + brand3, exception3.getMessage());
    }

    @Test
    @DisplayName("Dado una marca vacía, cuando se crea InvalidBrandException, entonces la excepción debe manejar correctamente la marca vacía")
    void givenEmptyBrand_whenCreatingException_thenShouldHandleEmptyBrandCorrectly() {
        // Given
        String emptyBrand = "";

        // When
        InvalidBrandException exception = new InvalidBrandException(emptyBrand);

        // Then
        assertEquals("Invalid brand: " + emptyBrand, exception.getMessage());
    }

    @Test
    @DisplayName("Dado una marca nula, cuando se crea InvalidBrandException, entonces la excepción debe manejar correctamente la marca nula")
    void givenNullBrand_whenCreatingException_thenShouldHandleNullBrandCorrectly() {
        // Given
        String nullBrand = null;

        // When
        InvalidBrandException exception = new InvalidBrandException(nullBrand);

        // Then
        assertEquals("Invalid brand: null", exception.getMessage());
    }

    @Test
    @DisplayName("Dado una InvalidBrandException, cuando se verifica la herencia, entonces debe ser instancia de ProductValueObjectException")
    void givenInvalidBrandException_whenCheckingInheritance_thenShouldBeProductValueObjectException() {
        // Given
        String brand = "TestBrand";

        // When
        InvalidBrandException exception = new InvalidBrandException(brand);

        // Then
        assertInstanceOf(ProductValueObjectException.class, exception);
    }

    @Test
    @DisplayName("Dado una InvalidBrandException, cuando se verifica la herencia completa, entonces debe ser instancia de RuntimeException")
    void givenInvalidBrandException_whenCheckingFullInheritance_thenShouldBeRuntimeException() {
        // Given
        String brand = "TestBrand";

        // When
        InvalidBrandException exception = new InvalidBrandException(brand);

        // Then
        assertInstanceOf(RuntimeException.class, exception);
    }

    @Test
    @DisplayName("Dado una InvalidBrandException, cuando se obtiene el stack trace, entonces debe estar disponible para depuración")
    void givenInvalidBrandException_whenGettingStackTrace_thenShouldBeAvailableForDebugging() {
        // Given
        String brand = "DebugBrand";

        // When
        InvalidBrandException exception = new InvalidBrandException(brand);

        // Then
        assertNotNull(exception.getStackTrace());
        assertTrue(exception.getStackTrace().length > 0);
    }

    @Test
    @DisplayName("Dado marcas con espacios, cuando se crea InvalidBrandException, entonces debe incluir los espacios en el mensaje")
    void givenBrandWithSpaces_whenCreatingException_thenShouldIncludeSpacesInMessage() {
        // Given
        String brandWithSpaces = "Brand With Spaces";

        // When
        InvalidBrandException exception = new InvalidBrandException(brandWithSpaces);

        // Then
        assertEquals("Invalid brand: " + brandWithSpaces, exception.getMessage());
    }
}