package com.linktic.challenge.products.unit.domain.exception.entity;

import com.linktic.challenge.products.domain.exception.entity.ProductAlreadyExistsException;
import com.linktic.challenge.products.domain.exception.entity.ProductEntityException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductAlreadyExistsExceptionTest {

    @Test
    @DisplayName("Dado un nombre de producto, cuando se crea ProductAlreadyExistsException, entonces la excepción debe contener el nombre del producto y el mensaje correcto")
    void givenProductName_whenCreatingException_thenShouldContainProductNameAndCorrectMessage() {
        // Given
        String productName = "Laptop Gaming";

        // When
        ProductAlreadyExistsException exception = new ProductAlreadyExistsException(productName);

        // Then
        assertEquals(productName, exception.getProductName());
        assertEquals("Product already exists with name: " + productName, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    @DisplayName("Dado diferentes nombres de producto, cuando se crea ProductAlreadyExistsException, entonces cada excepción debe contener su respectivo nombre y mensaje")
    void givenDifferentProductNames_whenCreatingExceptions_thenEachShouldContainRespectiveNameAndMessage() {
        // Given
        String productName1 = "Smartphone";
        String productName2 = "Tablet";
        String productName3 = "Headphones";

        // When
        ProductAlreadyExistsException exception1 = new ProductAlreadyExistsException(productName1);
        ProductAlreadyExistsException exception2 = new ProductAlreadyExistsException(productName2);
        ProductAlreadyExistsException exception3 = new ProductAlreadyExistsException(productName3);

        // Then
        assertEquals(productName1, exception1.getProductName());
        assertEquals("Product already exists with name: " + productName1, exception1.getMessage());

        assertEquals(productName2, exception2.getProductName());
        assertEquals("Product already exists with name: " + productName2, exception2.getMessage());

        assertEquals(productName3, exception3.getProductName());
        assertEquals("Product already exists with name: " + productName3, exception3.getMessage());
    }

    @Test
    @DisplayName("Dado un nombre de producto vacío, cuando se crea ProductAlreadyExistsException, entonces la excepción debe manejar correctamente el nombre vacío")
    void givenEmptyProductName_whenCreatingException_thenShouldHandleEmptyNameCorrectly() {
        // Given
        String emptyProductName = "";

        // When
        ProductAlreadyExistsException exception = new ProductAlreadyExistsException(emptyProductName);

        // Then
        assertEquals(emptyProductName, exception.getProductName());
        assertEquals("Product already exists with name: " + emptyProductName, exception.getMessage());
    }

    @Test
    @DisplayName("Dado un nombre de producto nulo, cuando se crea ProductAlreadyExistsException, entonces la excepción debe manejar correctamente el valor nulo")
    void givenNullProductName_whenCreatingException_thenShouldHandleNullCorrectly() {

        // When
        ProductAlreadyExistsException exception = new ProductAlreadyExistsException(null);

        // Then
        assertNull(exception.getProductName());
        assertEquals("Product already exists with name: null", exception.getMessage());
    }

    @Test
    @DisplayName("Dado una ProductAlreadyExistsException, cuando se verifica la herencia, entonces debe ser instancia de ProductEntityException")
    void givenProductAlreadyExistsException_whenCheckingInheritance_thenShouldBeProductEntityException() {
        // Given
        String productName = "Test Product";

        // When
        ProductAlreadyExistsException exception = new ProductAlreadyExistsException(productName);

        // Then
        assertInstanceOf(ProductEntityException.class, exception);
    }

    @Test
    @DisplayName("Dado una ProductAlreadyExistsException, cuando se verifica la herencia completa, entonces debe ser instancia de RuntimeException")
    void givenProductAlreadyExistsException_whenCheckingFullInheritance_thenShouldBeRuntimeException() {
        // Given
        String productName = "Test Product";

        // When
        ProductAlreadyExistsException exception = new ProductAlreadyExistsException(productName);

        // Then
        assertInstanceOf(RuntimeException.class, exception);
    }

    @Test
    @DisplayName("Dado una ProductAlreadyExistsException, cuando se accede al producto, entonces debe retornar el nombre correcto del producto")
    void givenProductAlreadyExistsException_whenAccessingProduct_thenShouldReturnCorrectProductName() {
        // Given
        String expectedProductName = "Gaming Mouse";
        ProductAlreadyExistsException exception = new ProductAlreadyExistsException(expectedProductName);

        // When
        String actualProductName = exception.getProductName();

        // Then
        assertEquals(expectedProductName, actualProductName);
    }
}