package com.linktic.challenge.products.unit.domain.exception.valueobject;

import com.linktic.challenge.products.domain.exception.valueobject.InvalidProductIdException;
import com.linktic.challenge.products.domain.exception.valueobject.ProductValueObjectException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InvalidProductIdExceptionTest {

    @Test
    @DisplayName("Dado un mensaje de error de ID de producto, cuando se crea InvalidProductIdException, entonces la excepción debe contener el mensaje proporcionado")
    void givenProductIdErrorMessage_whenCreatingException_thenShouldContainProvidedMessage() {
        // Given
        String expectedMessage = "Product ID cannot be empty";

        // When
        InvalidProductIdException exception = new InvalidProductIdException(expectedMessage);

        // Then
        assertEquals(expectedMessage, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    @DisplayName("Dado diferentes mensajes de error de ID de producto, cuando se crea InvalidProductIdException, entonces cada excepción debe contener su mensaje respectivo")
    void givenDifferentProductIdErrorMessages_whenCreatingExceptions_thenEachShouldContainRespectiveMessage() {
        // Given
        String message1 = "Product ID format is invalid";
        String message2 = "Product ID must be a valid UUID";
        String message3 = "Product ID exceeds maximum length";

        // When
        InvalidProductIdException exception1 = new InvalidProductIdException(message1);
        InvalidProductIdException exception2 = new InvalidProductIdException(message2);
        InvalidProductIdException exception3 = new InvalidProductIdException(message3);

        // Then
        assertEquals(message1, exception1.getMessage());
        assertEquals(message2, exception2.getMessage());
        assertEquals(message3, exception3.getMessage());
    }

    @Test
    @DisplayName("Dado un mensaje de error vacío, cuando se crea InvalidProductIdException, entonces la excepción debe manejar correctamente el mensaje vacío")
    void givenEmptyErrorMessage_whenCreatingException_thenShouldHandleEmptyMessageCorrectly() {
        // Given
        String emptyMessage = "";

        // When
        InvalidProductIdException exception = new InvalidProductIdException(emptyMessage);

        // Then
        assertEquals(emptyMessage, exception.getMessage());
    }

    @Test
    @DisplayName("Dado un mensaje de error nulo, cuando se crea InvalidProductIdException, entonces la excepción debe manejar correctamente el mensaje nulo")
    void givenNullErrorMessage_whenCreatingException_thenShouldHandleNullMessageCorrectly() {
        // When
        InvalidProductIdException exception = new InvalidProductIdException(null);

        // Then
        assertNull(exception.getMessage());
    }

    @Test
    @DisplayName("Dado una InvalidProductIdException, cuando se verifica la herencia, entonces debe ser instancia de ProductValueObjectException")
    void givenInvalidProductIdException_whenCheckingInheritance_thenShouldBeProductValueObjectException() {
        // Given
        String errorMessage = "Invalid product ID";

        // When
        InvalidProductIdException exception = new InvalidProductIdException(errorMessage);

        // Then
        assertInstanceOf(ProductValueObjectException.class, exception);
    }

    @Test
    @DisplayName("Dado una InvalidProductIdException, cuando se verifica la herencia completa, entonces debe ser instancia de RuntimeException")
    void givenInvalidProductIdException_whenCheckingFullInheritance_thenShouldBeRuntimeException() {
        // Given
        String errorMessage = "Invalid product ID";

        // When
        InvalidProductIdException exception = new InvalidProductIdException(errorMessage);

        // Then
        assertInstanceOf(RuntimeException.class, exception);
    }

    @Test
    @DisplayName("Dado una InvalidProductIdException, cuando se obtiene el stack trace, entonces debe estar disponible para depuración")
    void givenInvalidProductIdException_whenGettingStackTrace_thenShouldBeAvailableForDebugging() {
        // Given
        String errorMessage = "Product ID validation failed";

        // When
        InvalidProductIdException exception = new InvalidProductIdException(errorMessage);

        // Then
        assertNotNull(exception.getStackTrace());
        assertTrue(exception.getStackTrace().length > 0);
    }

    @Test
    @DisplayName("Dado mensajes de error específicos de formatos de ID, cuando se crea InvalidProductIdException, entonces debe aceptar cualquier mensaje personalizado")
    void givenIdFormatSpecificErrorMessages_whenCreatingException_thenShouldAcceptAnyCustomMessage() {
        // Given
        String uuidError = "Product ID must be a valid UUID version 4";
        String numericError = "Product ID must contain only alphanumeric characters";
        String lengthError = "Product ID must be between 5 and 50 characters long";

        // When
        InvalidProductIdException uuidException = new InvalidProductIdException(uuidError);
        InvalidProductIdException numericException = new InvalidProductIdException(numericError);
        InvalidProductIdException lengthException = new InvalidProductIdException(lengthError);

        // Then
        assertEquals(uuidError, uuidException.getMessage());
        assertEquals(numericError, numericException.getMessage());
        assertEquals(lengthError, lengthException.getMessage());
    }

    @Test
    @DisplayName("Dado mensajes de error con ejemplos específicos, cuando se crea InvalidProductIdException, entonces debe preservar los ejemplos en el mensaje")
    void givenErrorMessagesWithExamples_whenCreatingException_thenShouldPreserveExamples() {
        // Given
        String exampleMessage = "Invalid product ID: '123'. Expected format: PROD-XXXXX where X is alphanumeric";
        String regexMessage = "Product ID 'AB@123' does not match pattern: [A-Z0-9-]+";

        // When
        InvalidProductIdException exampleException = new InvalidProductIdException(exampleMessage);
        InvalidProductIdException regexException = new InvalidProductIdException(regexMessage);

        // Then
        assertEquals(exampleMessage, exampleException.getMessage());
        assertEquals(regexMessage, regexException.getMessage());
    }

    @Test
    @DisplayName("Dado mensajes de error de ID con referencias a sistemas externos, cuando se crea InvalidProductIdException, entonces debe manejar referencias correctamente")
    void givenErrorMessagesWithExternalReferences_whenCreatingException_thenShouldHandleReferencesCorrectly() {
        // Given
        String externalRefMessage = "Product ID 'LEGACY-001' is not compatible with new system format";
        String migrationMessage = "Product ID migration failed for identifier: OLD_PROD_12345";

        // When
        InvalidProductIdException externalRefException = new InvalidProductIdException(externalRefMessage);
        InvalidProductIdException migrationException = new InvalidProductIdException(migrationMessage);

        // Then
        assertEquals(externalRefMessage, externalRefException.getMessage());
        assertEquals(migrationMessage, migrationException.getMessage());
    }
}