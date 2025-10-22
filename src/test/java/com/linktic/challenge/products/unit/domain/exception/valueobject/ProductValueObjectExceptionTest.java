package com.linktic.challenge.products.unit.domain.exception.valueobject;

import com.linktic.challenge.products.domain.exception.ProductDomainException;
import com.linktic.challenge.products.domain.exception.valueobject.ProductValueObjectException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductValueObjectExceptionTest {

    @Test
    @DisplayName("Dado un mensaje de excepción personalizado, cuando se crea ProductValueObjectException, entonces la excepción debe contener el mensaje proporcionado")
    void givenCustomMessage_whenCreatingException_thenShouldContainProvidedMessage() {
        // Given
        String expectedMessage = "Product value object validation failed";

        // When
        ProductValueObjectException exception = new ProductValueObjectException(expectedMessage) {};

        // Then
        assertEquals(expectedMessage, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    @DisplayName("Dado diferentes mensajes de error, cuando se crea ProductValueObjectException, entonces cada excepción debe contener su mensaje respectivo")
    void givenDifferentErrorMessages_whenCreatingExceptions_thenEachShouldContainRespectiveMessage() {
        // Given
        String message1 = "Invalid price value";
        String message2 = "Invalid product name";
        String message3 = "Invalid stock quantity";

        // When
        ProductValueObjectException exception1 = new ProductValueObjectException(message1) {};
        ProductValueObjectException exception2 = new ProductValueObjectException(message2) {};
        ProductValueObjectException exception3 = new ProductValueObjectException(message3) {};

        // Then
        assertEquals(message1, exception1.getMessage());
        assertEquals(message2, exception2.getMessage());
        assertEquals(message3, exception3.getMessage());
    }

    @Test
    @DisplayName("Dado una ProductValueObjectException, cuando se verifica la herencia, entonces debe ser instancia de ProductDomainException")
    void givenProductValueObjectException_whenCheckingInheritance_thenShouldBeProductDomainException() {
        // Given
        String errorMessage = "Value object validation error";

        // When
        ProductValueObjectException exception = new ProductValueObjectException(errorMessage) {};

        // Then
        assertInstanceOf(ProductDomainException.class, exception);
    }

    @Test
    @DisplayName("Dado una ProductValueObjectException, cuando se verifica la herencia completa, entonces debe ser instancia de RuntimeException")
    void givenProductValueObjectException_whenCheckingFullInheritance_thenShouldBeRuntimeException() {
        // Given
        String errorMessage = "Value object validation error";

        // When
        ProductValueObjectException exception = new ProductValueObjectException(errorMessage) {};

        // Then
        assertInstanceOf(RuntimeException.class, exception);
    }

    @Test
    @DisplayName("Dado una ProductValueObjectException, cuando se obtiene el stack trace, entonces debe estar disponible para depuración")
    void givenProductValueObjectException_whenGettingStackTrace_thenShouldBeAvailableForDebugging() {
        // Given
        String errorMessage = "Invalid value object state";

        // When
        ProductValueObjectException exception = new ProductValueObjectException(errorMessage) {};

        // Then
        assertNotNull(exception.getStackTrace());
        assertTrue(exception.getStackTrace().length > 0);
    }

    @Test
    @DisplayName("Dado un mensaje de error vacío, cuando se crea ProductValueObjectException, entonces la excepción debe manejar correctamente el mensaje vacío")
    void givenEmptyErrorMessage_whenCreatingException_thenShouldHandleEmptyMessageCorrectly() {
        // Given
        String emptyMessage = "";

        // When
        ProductValueObjectException exception = new ProductValueObjectException(emptyMessage) {};

        // Then
        assertEquals(emptyMessage, exception.getMessage());
    }

    @Test
    @DisplayName("Dado un mensaje de error nulo, cuando se crea ProductValueObjectException, entonces la excepción debe manejar correctamente el mensaje nulo")
    void givenNullErrorMessage_whenCreatingException_thenShouldHandleNullMessageCorrectly() {

        // When
        ProductValueObjectException exception = new ProductValueObjectException(null) {};

        // Then
        assertNull(exception.getMessage());
    }
}