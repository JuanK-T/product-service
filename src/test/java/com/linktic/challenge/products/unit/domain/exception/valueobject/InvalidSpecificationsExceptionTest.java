package com.linktic.challenge.products.unit.domain.exception.valueobject;

import com.linktic.challenge.products.domain.exception.valueobject.InvalidSpecificationsException;
import com.linktic.challenge.products.domain.exception.valueobject.ProductValueObjectException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InvalidSpecificationsExceptionTest {

    @Test
    @DisplayName("Dado un mensaje de especificaciones inválidas, cuando se crea InvalidSpecificationsException, entonces la excepción debe contener el mensaje correcto con el detalle")
    void givenSpecificationsMessage_whenCreatingException_thenShouldContainCorrectMessageWithDetail() {
        // Given
        String message = "Specifications cannot be empty";

        // When
        InvalidSpecificationsException exception = new InvalidSpecificationsException(message);

        // Then
        assertEquals("Invalid specifications: " + message, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    @DisplayName("Dado diferentes mensajes de especificaciones inválidas, cuando se crea InvalidSpecificationsException, entonces cada excepción debe contener su respectivo detalle en el mensaje")
    void givenDifferentSpecificationsMessages_whenCreatingExceptions_thenEachShouldContainRespectiveDetailInMessage() {
        // Given
        String message1 = "JSON format is invalid";
        String message2 = "Maximum specifications size exceeded";
        String message3 = "Required fields are missing";

        // When
        InvalidSpecificationsException exception1 = new InvalidSpecificationsException(message1);
        InvalidSpecificationsException exception2 = new InvalidSpecificationsException(message2);
        InvalidSpecificationsException exception3 = new InvalidSpecificationsException(message3);

        // Then
        assertEquals("Invalid specifications: " + message1, exception1.getMessage());
        assertEquals("Invalid specifications: " + message2, exception2.getMessage());
        assertEquals("Invalid specifications: " + message3, exception3.getMessage());
    }

    @Test
    @DisplayName("Dado un mensaje vacío, cuando se crea InvalidSpecificationsException, entonces la excepción debe manejar correctamente el mensaje vacío")
    void givenEmptyMessage_whenCreatingException_thenShouldHandleEmptyMessageCorrectly() {
        // Given
        String emptyMessage = "";

        // When
        InvalidSpecificationsException exception = new InvalidSpecificationsException(emptyMessage);

        // Then
        assertEquals("Invalid specifications: " + emptyMessage, exception.getMessage());
    }

    @Test
    @DisplayName("Dado un mensaje nulo, cuando se crea InvalidSpecificationsException, entonces la excepción debe manejar correctamente el mensaje nulo")
    void givenNullMessage_whenCreatingException_thenShouldHandleNullMessageCorrectly() {
        // When
        InvalidSpecificationsException exception = new InvalidSpecificationsException(null);

        // Then
        assertEquals("Invalid specifications: null", exception.getMessage());
    }

    @Test
    @DisplayName("Dado una InvalidSpecificationsException, cuando se verifica la herencia, entonces debe ser instancia de ProductValueObjectException")
    void givenInvalidSpecificationsException_whenCheckingInheritance_thenShouldBeProductValueObjectException() {
        // Given
        String message = "Specifications validation failed";

        // When
        InvalidSpecificationsException exception = new InvalidSpecificationsException(message);

        // Then
        assertInstanceOf(ProductValueObjectException.class, exception);
    }

    @Test
    @DisplayName("Dado una InvalidSpecificationsException, cuando se verifica la herencia completa, entonces debe ser instancia de RuntimeException")
    void givenInvalidSpecificationsException_whenCheckingFullInheritance_thenShouldBeRuntimeException() {
        // Given
        String message = "Specifications validation failed";

        // When
        InvalidSpecificationsException exception = new InvalidSpecificationsException(message);

        // Then
        assertInstanceOf(RuntimeException.class, exception);
    }

    @Test
    @DisplayName("Dado una InvalidSpecificationsException, cuando se obtiene el stack trace, entonces debe estar disponible para depuración")
    void givenInvalidSpecificationsException_whenGettingStackTrace_thenShouldBeAvailableForDebugging() {
        // Given
        String message = "Debug specifications error";

        // When
        InvalidSpecificationsException exception = new InvalidSpecificationsException(message);

        // Then
        assertNotNull(exception.getStackTrace());
        assertTrue(exception.getStackTrace().length > 0);
    }

    @Test
    @DisplayName("Dado mensajes con detalles de formato JSON, cuando se crea InvalidSpecificationsException, entonces debe incluir los detalles técnicos en el mensaje")
    void givenJsonFormatMessages_whenCreatingException_thenShouldIncludeTechnicalDetails() {
        // Given
        String jsonMessage1 = "Invalid JSON syntax at line 3, column 15";
        String jsonMessage2 = "Missing required field: 'dimensions'";
        String jsonMessage3 = "Field 'weight' must be a positive number";

        // When
        InvalidSpecificationsException exception1 = new InvalidSpecificationsException(jsonMessage1);
        InvalidSpecificationsException exception2 = new InvalidSpecificationsException(jsonMessage2);
        InvalidSpecificationsException exception3 = new InvalidSpecificationsException(jsonMessage3);

        // Then
        assertEquals("Invalid specifications: " + jsonMessage1, exception1.getMessage());
        assertEquals("Invalid specifications: " + jsonMessage2, exception2.getMessage());
        assertEquals("Invalid specifications: " + jsonMessage3, exception3.getMessage());
    }

    @Test
    @DisplayName("Dado mensajes con límites de tamaño y longitud, cuando se crea InvalidSpecificationsException, entonces debe manejar correctamente las restricciones")
    void givenSizeLimitMessages_whenCreatingException_thenShouldHandleConstraintsCorrectly() {
        // Given
        String sizeMessage1 = "Specifications exceed maximum size of 10KB";
        String sizeMessage2 = "Too many specification items: 150, maximum allowed: 100";
        String sizeMessage3 = "Specification value too long: 500 characters, maximum: 255";

        // When
        InvalidSpecificationsException exception1 = new InvalidSpecificationsException(sizeMessage1);
        InvalidSpecificationsException exception2 = new InvalidSpecificationsException(sizeMessage2);
        InvalidSpecificationsException exception3 = new InvalidSpecificationsException(sizeMessage3);

        // Then
        assertEquals("Invalid specifications: " + sizeMessage1, exception1.getMessage());
        assertEquals("Invalid specifications: " + sizeMessage2, exception2.getMessage());
        assertEquals("Invalid specifications: " + sizeMessage3, exception3.getMessage());
    }

    @Test
    @DisplayName("Dado mensajes con reglas de negocio específicas, cuando se crea InvalidSpecificationsException, entonces debe aceptar cualquier regla de negocio")
    void givenBusinessRuleMessages_whenCreatingException_thenShouldAcceptAnyBusinessRule() {
        // Given
        String businessRule1 = "Electronics products must include 'warranty' field";
        String businessRule2 = "Clothing products require 'size' and 'color' specifications";
        String businessRule3 = "Food products must include 'expiration_date' and 'ingredients'";

        // When
        InvalidSpecificationsException exception1 = new InvalidSpecificationsException(businessRule1);
        InvalidSpecificationsException exception2 = new InvalidSpecificationsException(businessRule2);
        InvalidSpecificationsException exception3 = new InvalidSpecificationsException(businessRule3);

        // Then
        assertEquals("Invalid specifications: " + businessRule1, exception1.getMessage());
        assertEquals("Invalid specifications: " + businessRule2, exception2.getMessage());
        assertEquals("Invalid specifications: " + businessRule3, exception3.getMessage());
    }

    @Test
    @DisplayName("Dado mensajes con tipos de datos inválidos, cuando se crea InvalidSpecificationsException, entonces debe manejar correctamente los tipos de datos")
    void givenDataTypeMessages_whenCreatingException_thenShouldHandleDataTypesCorrectly() {
        // Given
        String dataTypeMessage1 = "Field 'price' must be a number, received: 'expensive'";
        String dataTypeMessage2 = "Field 'in_stock' must be boolean, received: 'yes'";
        String dataTypeMessage3 = "Field 'features' must be an array, received: object";

        // When
        InvalidSpecificationsException exception1 = new InvalidSpecificationsException(dataTypeMessage1);
        InvalidSpecificationsException exception2 = new InvalidSpecificationsException(dataTypeMessage2);
        InvalidSpecificationsException exception3 = new InvalidSpecificationsException(dataTypeMessage3);

        // Then
        assertEquals("Invalid specifications: " + dataTypeMessage1, exception1.getMessage());
        assertEquals("Invalid specifications: " + dataTypeMessage2, exception2.getMessage());
        assertEquals("Invalid specifications: " + dataTypeMessage3, exception3.getMessage());
    }

    @Test
    @DisplayName("Dado mensajes con validaciones de categorías, cuando se crea InvalidSpecificationsException, entonces debe manejar validaciones por categoría")
    void givenCategoryValidationMessages_whenCreatingException_thenShouldHandleCategoryValidations() {
        // Given
        String categoryMessage1 = "For category 'Electronics', field 'power_consumption' is required";
        String categoryMessage2 = "Category 'Books' does not allow specification 'weight'";
        String categoryMessage3 = "Category 'Furniture' requires 'dimensions' and 'material' fields";

        // When
        InvalidSpecificationsException exception1 = new InvalidSpecificationsException(categoryMessage1);
        InvalidSpecificationsException exception2 = new InvalidSpecificationsException(categoryMessage2);
        InvalidSpecificationsException exception3 = new InvalidSpecificationsException(categoryMessage3);

        // Then
        assertEquals("Invalid specifications: " + categoryMessage1, exception1.getMessage());
        assertEquals("Invalid specifications: " + categoryMessage2, exception2.getMessage());
        assertEquals("Invalid specifications: " + categoryMessage3, exception3.getMessage());
    }

    @Test
    @DisplayName("Dado mensajes con contextos de productos específicos, cuando se crea InvalidSpecificationsException, entonces debe preservar el contexto completo")
    void givenProductContextMessages_whenCreatingException_thenShouldPreserveCompleteContext() {
        // Given
        String contextMessage1 = "Product PROD-123: Missing required specification 'color'";
        String contextMessage2 = "Smartphone specifications: Invalid value for 'storage' - must be one of [64, 128, 256, 512]";
        String contextMessage3 = "Laptop specifications: 'processor' field format invalid, expected: 'Intel i7-12700H' or similar";

        // When
        InvalidSpecificationsException exception1 = new InvalidSpecificationsException(contextMessage1);
        InvalidSpecificationsException exception2 = new InvalidSpecificationsException(contextMessage2);
        InvalidSpecificationsException exception3 = new InvalidSpecificationsException(contextMessage3);

        // Then
        assertEquals("Invalid specifications: " + contextMessage1, exception1.getMessage());
        assertEquals("Invalid specifications: " + contextMessage2, exception2.getMessage());
        assertEquals("Invalid specifications: " + contextMessage3, exception3.getMessage());
    }
}