package com.linktic.challenge.products.unit.domain.exception.entity;

import com.linktic.challenge.products.domain.exception.ProductDomainException;
import com.linktic.challenge.products.domain.exception.entity.ProductEntityException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductEntityExceptionTest {

    @Test
    @DisplayName("Dado un mensaje de excepción personalizado, cuando se crea ProductEntityException, entonces la excepción debe contener el mensaje proporcionado")
    void givenCustomMessage_whenCreatingException_thenShouldContainProvidedMessage() {
        // Given
        String expectedMessage = "Product entity validation failed";

        // When
        ProductEntityException exception = new ProductEntityException(expectedMessage) {};

        // Then
        assertEquals(expectedMessage, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    @DisplayName("Dado un mensaje de excepción y una causa, cuando se crea ProductEntityException, entonces la excepción debe contener tanto el mensaje como la causa")
    void givenCustomMessageAndCause_whenCreatingException_thenShouldContainMessageAndCause() {
        // Given
        String expectedMessage = "Product entity validation failed";
        Throwable expectedCause = new IllegalArgumentException("Invalid entity data");

        // When
        ProductEntityException exception = new ProductEntityException(expectedMessage, expectedCause) {};

        // Then
        assertEquals(expectedMessage, exception.getMessage());
        assertEquals(expectedCause, exception.getCause());
    }

    @Test
    @DisplayName("Dado una ProductEntityException, cuando se verifica la herencia, entonces debe ser instancia de ProductDomainException")
    void givenProductEntityException_whenCheckingInheritance_thenShouldBeProductDomainException() {
        // Given
        String errorMessage = "Test error message";

        // When
        ProductEntityException exception = new ProductEntityException(errorMessage) {};

        // Then
        assertInstanceOf(ProductDomainException.class, exception);
    }

    @Test
    @DisplayName("Dado una ProductEntityException, cuando se verifica la herencia completa, entonces debe ser instancia de RuntimeException")
    void givenProductEntityException_whenCheckingFullInheritance_thenShouldBeRuntimeException() {
        // Given
        String errorMessage = "Test error message";

        // When
        ProductEntityException exception = new ProductEntityException(errorMessage) {};

        // Then
        assertInstanceOf(RuntimeException.class, exception);
    }

    @Test
    @DisplayName("Dado una ProductEntityException con causa, cuando se obtiene la causa, entonces debe ser la misma que se proporcionó")
    void givenExceptionWithCause_whenGettingCause_thenShouldBeTheSameProvided() {
        // Given
        String expectedMessage = "Product entity error";
        NullPointerException expectedCause = new NullPointerException("Null value detected");

        // When
        ProductEntityException exception = new ProductEntityException(expectedMessage, expectedCause) {};

        // Then
        assertSame(expectedCause, exception.getCause());
        assertEquals("Null value detected", exception.getCause().getMessage());
    }
}