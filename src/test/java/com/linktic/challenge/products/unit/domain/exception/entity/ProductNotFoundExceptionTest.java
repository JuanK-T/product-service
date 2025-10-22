package com.linktic.challenge.products.unit.domain.exception.entity;

import com.linktic.challenge.products.domain.exception.entity.ProductEntityException;
import com.linktic.challenge.products.domain.exception.entity.ProductNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductNotFoundExceptionTest {

    @Test
    @DisplayName("Dado un ID de producto, cuando se crea ProductNotFoundException, entonces la excepción debe contener el ID y el mensaje correcto")
    void givenProductId_whenCreatingException_thenShouldContainProductIdAndCorrectMessage() {
        // Given
        String productId = "PROD-12345";

        // When
        ProductNotFoundException exception = new ProductNotFoundException(productId);

        // Then
        assertEquals(productId, exception.getProductId());
        assertEquals("Product not found with ID: " + productId, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    @DisplayName("Dado diferentes IDs de producto, cuando se crea ProductNotFoundException, entonces cada excepción debe contener su respectivo ID y mensaje")
    void givenDifferentProductIds_whenCreatingExceptions_thenEachShouldContainRespectiveIdAndMessage() {
        // Given
        String productId1 = "PROD-001";
        String productId2 = "PROD-002";
        String productId3 = "PROD-003";

        // When
        ProductNotFoundException exception1 = new ProductNotFoundException(productId1);
        ProductNotFoundException exception2 = new ProductNotFoundException(productId2);
        ProductNotFoundException exception3 = new ProductNotFoundException(productId3);

        // Then
        assertEquals(productId1, exception1.getProductId());
        assertEquals("Product not found with ID: " + productId1, exception1.getMessage());

        assertEquals(productId2, exception2.getProductId());
        assertEquals("Product not found with ID: " + productId2, exception2.getMessage());

        assertEquals(productId3, exception3.getProductId());
        assertEquals("Product not found with ID: " + productId3, exception3.getMessage());
    }

    @Test
    @DisplayName("Dado un ID de producto vacío, cuando se crea ProductNotFoundException, entonces la excepción debe manejar correctamente el ID vacío")
    void givenEmptyProductId_whenCreatingException_thenShouldHandleEmptyIdCorrectly() {
        // Given
        String emptyProductId = "";

        // When
        ProductNotFoundException exception = new ProductNotFoundException(emptyProductId);

        // Then
        assertEquals(emptyProductId, exception.getProductId());
        assertEquals("Product not found with ID: " + emptyProductId, exception.getMessage());
    }

    @Test
    @DisplayName("Dado un ID de producto nulo, cuando se crea ProductNotFoundException, entonces la excepción debe manejar correctamente el valor nulo")
    void givenNullProductId_whenCreatingException_thenShouldHandleNullCorrectly() {
        // When
        ProductNotFoundException exception = new ProductNotFoundException(null);

        // Then
        assertNull(exception.getProductId());
        assertEquals("Product not found with ID: null", exception.getMessage());
    }

    @Test
    @DisplayName("Dado una ProductNotFoundException, cuando se verifica la herencia, entonces debe ser instancia de ProductEntityException")
    void givenProductNotFoundException_whenCheckingInheritance_thenShouldBeProductEntityException() {
        // Given
        String productId = "TEST-123";

        // When
        ProductNotFoundException exception = new ProductNotFoundException(productId);

        // Then
        assertInstanceOf(ProductEntityException.class, exception);
    }

    @Test
    @DisplayName("Dado una ProductNotFoundException, cuando se verifica la herencia completa, entonces debe ser instancia de RuntimeException")
    void givenProductNotFoundException_whenCheckingFullInheritance_thenShouldBeRuntimeException() {
        // Given
        String productId = "TEST-123";

        // When
        ProductNotFoundException exception = new ProductNotFoundException(productId);

        // Then
        assertInstanceOf(RuntimeException.class, exception);
    }

    @Test
    @DisplayName("Dado una ProductNotFoundException, cuando se accede al ID del producto, entonces debe retornar el ID correcto")
    void givenProductNotFoundException_whenAccessingProductId_thenShouldReturnCorrectProductId() {
        // Given
        String expectedProductId = "PROD-999";
        ProductNotFoundException exception = new ProductNotFoundException(expectedProductId);

        // When
        String actualProductId = exception.getProductId();

        // Then
        assertEquals(expectedProductId, actualProductId);
    }

    @Test
    @DisplayName("Dado IDs de producto con formatos especiales, cuando se crea ProductNotFoundException, entonces debe manejar correctamente los formatos especiales")
    void givenSpecialFormatProductIds_whenCreatingException_thenShouldHandleSpecialFormats() {
        // Given
        String uuidProductId = "123e4567-e89b-12d3-a456-426614174000";
        String numericProductId = "987654321";
        String specialCharProductId = "PROD@#$%";

        // When
        ProductNotFoundException exception1 = new ProductNotFoundException(uuidProductId);
        ProductNotFoundException exception2 = new ProductNotFoundException(numericProductId);
        ProductNotFoundException exception3 = new ProductNotFoundException(specialCharProductId);

        // Then
        assertEquals(uuidProductId, exception1.getProductId());
        assertEquals(numericProductId, exception2.getProductId());
        assertEquals(specialCharProductId, exception3.getProductId());
    }
}