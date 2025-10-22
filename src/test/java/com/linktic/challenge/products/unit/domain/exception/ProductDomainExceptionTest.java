package com.linktic.challenge.products.unit.domain.exception;

import com.linktic.challenge.products.domain.exception.ProductDomainException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductDomainExceptionTest {

    @Test
    @DisplayName("Dado un mensaje de excepción personalizado, cuando se crea ProductDomainException, entonces la excepción debe contener el mensaje proporcionado")
    void givenCustomMessage_whenCreatingException_thenShouldContainProvidedMessage() {
        // Given
        String expectedMessage = "Product domain validation failed";

        // When
        ProductDomainException exception = new ProductDomainException(expectedMessage) {};

        // Then
        assertEquals(expectedMessage, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    @DisplayName("Dado un mensaje de excepción y una causa, cuando se crea ProductDomainException, entonces la excepción debe contener tanto el mensaje como la causa")
    void givenCustomMessageAndCause_whenCreatingException_thenShouldContainMessageAndCause() {
        // Given
        String expectedMessage = "Product domain validation failed";
        Throwable expectedCause = new IllegalArgumentException("Invalid product data");

        // When
        ProductDomainException exception = new ProductDomainException(expectedMessage, expectedCause) {};

        // Then
        assertEquals(expectedMessage, exception.getMessage());
        assertEquals(expectedCause, exception.getCause());
    }

    @Test
    @DisplayName("Dado una ProductDomainException, cuando se verifica la herencia, entonces debe ser instancia de RuntimeException")
    void givenProductDomainException_whenCheckingInheritance_thenShouldBeRuntimeException() {
        // Given
        String errorMessage = "Test error message";

        // When
        ProductDomainException exception = new ProductDomainException(errorMessage) {};

        // Then
        assertInstanceOf(RuntimeException.class, exception);
    }
}