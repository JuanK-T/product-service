package com.linktic.challenge.products.unit.domain.exception.entity;

import com.linktic.challenge.products.domain.exception.ProductDomainException;
import com.linktic.challenge.products.domain.exception.entity.InvalidProductException;
import com.linktic.challenge.products.domain.exception.entity.ProductEntityException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InvalidProductExceptionTest {

    @Test
    @DisplayName("Dado un mensaje de error, cuando se crea InvalidProductException, entonces la excepción debe contener el mensaje proporcionado")
    void givenErrorMessage_whenCreatingInvalidProductException_thenShouldContainProvidedMessage() {
        // Given
        String expectedMessage = "Invalid product data provided";

        // When
        InvalidProductException exception = new InvalidProductException(expectedMessage);

        // Then
        assertEquals(expectedMessage, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    @DisplayName("Dado una InvalidProductException, cuando se verifica la herencia, entonces debe ser instancia de ProductEntityException")
    void givenInvalidProductException_whenCheckingInheritance_thenShouldBeProductEntityException() {
        // Given
        String errorMessage = "Test error message";

        // When
        InvalidProductException exception = new InvalidProductException(errorMessage);

        // Then
        assertInstanceOf(ProductEntityException.class, exception);
    }

    @Test
    @DisplayName("Dado una InvalidProductException, cuando se verifica la cadena de herencia, entonces debe ser instancia de ProductDomainException")
    void givenInvalidProductException_whenCheckingInheritanceChain_thenShouldBeProductDomainException() {
        // Given
        String errorMessage = "Test error message";

        // When
        InvalidProductException exception = new InvalidProductException(errorMessage);

        // Then
        assertInstanceOf(ProductDomainException.class, exception);
    }

    @Test
    @DisplayName("Dado una InvalidProductException, cuando se verifica la herencia completa, entonces debe ser instancia de RuntimeException")
    void givenInvalidProductException_whenCheckingFullInheritance_thenShouldBeRuntimeException() {
        // Given
        String errorMessage = "Product validation failed";

        // When
        InvalidProductException exception = new InvalidProductException(errorMessage);

        // Then
        assertInstanceOf(RuntimeException.class, exception);
    }

    @Test
    @DisplayName("Dado diferentes mensajes de error, cuando se crean múltiples InvalidProductException, entonces cada una debe contener su mensaje respectivo")
    void givenDifferentErrorMessages_whenCreatingMultipleExceptions_thenEachShouldContainRespectiveMessage() {
        // Given
        String message1 = "Product price cannot be negative";
        String message2 = "Product name cannot be empty";
        String message3 = "Product ID is required";

        // When
        InvalidProductException exception1 = new InvalidProductException(message1);
        InvalidProductException exception2 = new InvalidProductException(message2);
        InvalidProductException exception3 = new InvalidProductException(message3);

        // Then
        assertEquals(message1, exception1.getMessage());
        assertEquals(message2, exception2.getMessage());
        assertEquals(message3, exception3.getMessage());
    }

    @Test
    @DisplayName("Dado una InvalidProductException, cuando se obtiene el stack trace, entonces debe estar disponible para depuración")
    void givenInvalidProductException_whenGettingStackTrace_thenShouldBeAvailableForDebugging() {
        // Given
        String errorMessage = "Invalid product configuration";

        // When
        InvalidProductException exception = new InvalidProductException(errorMessage);

        // Then
        assertNotNull(exception.getStackTrace());
        assertTrue(exception.getStackTrace().length > 0);
    }
}