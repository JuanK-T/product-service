package com.linktic.challenge.products.unit.domain.exception.valueobject;

import com.linktic.challenge.products.domain.exception.valueobject.InvalidProductNameException;
import com.linktic.challenge.products.domain.exception.valueobject.ProductValueObjectException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InvalidProductNameExceptionTest {

    @Test
    @DisplayName("Dado un nombre de producto inválido, cuando se crea InvalidProductNameException, entonces la excepción debe contener el mensaje correcto con el nombre")
    void givenInvalidProductName_whenCreatingException_thenShouldContainCorrectMessageWithName() {
        // Given
        String productName = "Prod@Invalid#Name";

        // When
        InvalidProductNameException exception = new InvalidProductNameException(productName);

        // Then
        assertEquals("Invalid product name: " + productName, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    @DisplayName("Dado diferentes nombres de producto inválidos, cuando se crea InvalidProductNameException, entonces cada excepción debe contener su respectivo nombre en el mensaje")
    void givenDifferentInvalidProductNames_whenCreatingExceptions_thenEachShouldContainRespectiveNameInMessage() {
        // Given
        String name1 = "A"; // Too short
        String name2 = "ProductNameThatIsWayTooLongAndExceedsTheMaximumAllowedLengthForAProductNameInTheSystem";
        String name3 = "Product@With#$Special%Chars";

        // When
        InvalidProductNameException exception1 = new InvalidProductNameException(name1);
        InvalidProductNameException exception2 = new InvalidProductNameException(name2);
        InvalidProductNameException exception3 = new InvalidProductNameException(name3);

        // Then
        assertEquals("Invalid product name: " + name1, exception1.getMessage());
        assertEquals("Invalid product name: " + name2, exception2.getMessage());
        assertEquals("Invalid product name: " + name3, exception3.getMessage());
    }

    @Test
    @DisplayName("Dado un nombre de producto vacío, cuando se crea InvalidProductNameException, entonces la excepción debe manejar correctamente el nombre vacío")
    void givenEmptyProductName_whenCreatingException_thenShouldHandleEmptyNameCorrectly() {
        // Given
        String emptyName = "";

        // When
        InvalidProductNameException exception = new InvalidProductNameException(emptyName);

        // Then
        assertEquals("Invalid product name: " + emptyName, exception.getMessage());
    }

    @Test
    @DisplayName("Dado un nombre de producto nulo, cuando se crea InvalidProductNameException, entonces la excepción debe manejar correctamente el nombre nulo")
    void givenNullProductName_whenCreatingException_thenShouldHandleNullNameCorrectly() {
        // When
        InvalidProductNameException exception = new InvalidProductNameException(null);

        // Then
        assertEquals("Invalid product name: null", exception.getMessage());
    }

    @Test
    @DisplayName("Dado una InvalidProductNameException, cuando se verifica la herencia, entonces debe ser instancia de ProductValueObjectException")
    void givenInvalidProductNameException_whenCheckingInheritance_thenShouldBeProductValueObjectException() {
        // Given
        String productName = "TestProduct";

        // When
        InvalidProductNameException exception = new InvalidProductNameException(productName);

        // Then
        assertInstanceOf(ProductValueObjectException.class, exception);
    }

    @Test
    @DisplayName("Dado una InvalidProductNameException, cuando se verifica la herencia completa, entonces debe ser instancia de RuntimeException")
    void givenInvalidProductNameException_whenCheckingFullInheritance_thenShouldBeRuntimeException() {
        // Given
        String productName = "TestProduct";

        // When
        InvalidProductNameException exception = new InvalidProductNameException(productName);

        // Then
        assertInstanceOf(RuntimeException.class, exception);
    }

    @Test
    @DisplayName("Dado una InvalidProductNameException, cuando se obtiene el stack trace, entonces debe estar disponible para depuración")
    void givenInvalidProductNameException_whenGettingStackTrace_thenShouldBeAvailableForDebugging() {
        // Given
        String productName = "DebugProduct";

        // When
        InvalidProductNameException exception = new InvalidProductNameException(productName);

        // Then
        assertNotNull(exception.getStackTrace());
        assertTrue(exception.getStackTrace().length > 0);
    }

    @Test
    @DisplayName("Dado nombres de producto con espacios, cuando se crea InvalidProductNameException, entonces debe incluir los espacios en el mensaje")
    void givenProductNameWithSpaces_whenCreatingException_thenShouldIncludeSpacesInMessage() {
        // Given
        String nameWithSpaces = "  Product With  Multiple   Spaces  ";

        // When
        InvalidProductNameException exception = new InvalidProductNameException(nameWithSpaces);

        // Then
        assertEquals("Invalid product name: " + nameWithSpaces, exception.getMessage());
    }

    @Test
    @DisplayName("Dado nombres de producto con caracteres internacionales, cuando se crea InvalidProductNameException, entonces debe incluir todos los caracteres en el mensaje")
    void givenProductNameWithInternationalCharacters_whenCreatingException_thenShouldIncludeAllCharactersInMessage() {
        // Given
        String internationalName = "Café au Lait Español ñoño";
        String asianName = "产品名称测试";
        String russianName = "ТестовыйПродукт";

        // When
        InvalidProductNameException exception1 = new InvalidProductNameException(internationalName);
        InvalidProductNameException exception2 = new InvalidProductNameException(asianName);
        InvalidProductNameException exception3 = new InvalidProductNameException(russianName);

        // Then
        assertEquals("Invalid product name: " + internationalName, exception1.getMessage());
        assertEquals("Invalid product name: " + asianName, exception2.getMessage());
        assertEquals("Invalid product name: " + russianName, exception3.getMessage());
    }

    @Test
    @DisplayName("Dado nombres de producto que representan casos reales, cuando se crea InvalidProductNameException, entonces el mensaje debe ser informativo")
    void givenRealWorldProductNames_whenCreatingException_thenMessageShouldBeInformative() {
        // Given
        String name1 = "iPhone 14 Pro Max 256GB - Deep Purple";
        String name2 = "Samsung Galaxy S23 Ultra 5G 512GB";
        String name3 = "Laptop Gaming ASUS ROG Strix G15";

        // When
        InvalidProductNameException exception1 = new InvalidProductNameException(name1);
        InvalidProductNameException exception2 = new InvalidProductNameException(name2);
        InvalidProductNameException exception3 = new InvalidProductNameException(name3);

        // Then
        assertEquals("Invalid product name: " + name1, exception1.getMessage());
        assertEquals("Invalid product name: " + name2, exception2.getMessage());
        assertEquals("Invalid product name: " + name3, exception3.getMessage());
    }

    @Test
    @DisplayName("Dado nombres de producto con números y símbolos, cuando se crea InvalidProductNameException, entonces debe incluir todos los caracteres en el mensaje")
    void givenProductNameWithNumbersAndSymbols_whenCreatingException_thenShouldIncludeAllCharacters() {
        // Given
        String nameWithNumbers = "Product123 v4.5.2";
        String nameWithSymbols = "Product-Plus+ Edition";
        String mixedName = "Super_Product-2024@Special";

        // When
        InvalidProductNameException exception1 = new InvalidProductNameException(nameWithNumbers);
        InvalidProductNameException exception2 = new InvalidProductNameException(nameWithSymbols);
        InvalidProductNameException exception3 = new InvalidProductNameException(mixedName);

        // Then
        assertEquals("Invalid product name: " + nameWithNumbers, exception1.getMessage());
        assertEquals("Invalid product name: " + nameWithSymbols, exception2.getMessage());
        assertEquals("Invalid product name: " + mixedName, exception3.getMessage());
    }
}