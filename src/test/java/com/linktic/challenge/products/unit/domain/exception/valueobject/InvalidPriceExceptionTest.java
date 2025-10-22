package com.linktic.challenge.products.unit.domain.exception.valueobject;

import com.linktic.challenge.products.domain.exception.valueobject.InvalidPriceException;
import com.linktic.challenge.products.domain.exception.valueobject.ProductValueObjectException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InvalidPriceExceptionTest {

    @Test
    @DisplayName("Dado un mensaje de error de precio, cuando se crea InvalidPriceException, entonces la excepción debe contener el mensaje proporcionado")
    void givenPriceErrorMessage_whenCreatingException_thenShouldContainProvidedMessage() {
        // Given
        String expectedMessage = "Price cannot be negative";

        // When
        InvalidPriceException exception = new InvalidPriceException(expectedMessage);

        // Then
        assertEquals(expectedMessage, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    @DisplayName("Dado diferentes mensajes de error de precio, cuando se crea InvalidPriceException, entonces cada excepción debe contener su mensaje respectivo")
    void givenDifferentPriceErrorMessages_whenCreatingExceptions_thenEachShouldContainRespectiveMessage() {
        // Given
        String message1 = "Price must be greater than zero";
        String message2 = "Price format is invalid";
        String message3 = "Price exceeds maximum allowed value";

        // When
        InvalidPriceException exception1 = new InvalidPriceException(message1);
        InvalidPriceException exception2 = new InvalidPriceException(message2);
        InvalidPriceException exception3 = new InvalidPriceException(message3);

        // Then
        assertEquals(message1, exception1.getMessage());
        assertEquals(message2, exception2.getMessage());
        assertEquals(message3, exception3.getMessage());
    }

    @Test
    @DisplayName("Dado un mensaje de error vacío, cuando se crea InvalidPriceException, entonces la excepción debe manejar correctamente el mensaje vacío")
    void givenEmptyErrorMessage_whenCreatingException_thenShouldHandleEmptyMessageCorrectly() {
        // Given
        String emptyMessage = "";

        // When
        InvalidPriceException exception = new InvalidPriceException(emptyMessage);

        // Then
        assertEquals(emptyMessage, exception.getMessage());
    }

    @Test
    @DisplayName("Dado un mensaje de error nulo, cuando se crea InvalidPriceException, entonces la excepción debe manejar correctamente el mensaje nulo")
    void givenNullErrorMessage_whenCreatingException_thenShouldHandleNullMessageCorrectly() {
        // When
        InvalidPriceException exception = new InvalidPriceException(null);

        // Then
        assertNull(exception.getMessage());
    }

    @Test
    @DisplayName("Dado una InvalidPriceException, cuando se verifica la herencia, entonces debe ser instancia de ProductValueObjectException")
    void givenInvalidPriceException_whenCheckingInheritance_thenShouldBeProductValueObjectException() {
        // Given
        String errorMessage = "Invalid price value";

        // When
        InvalidPriceException exception = new InvalidPriceException(errorMessage);

        // Then
        assertInstanceOf(ProductValueObjectException.class, exception);
    }

    @Test
    @DisplayName("Dado una InvalidPriceException, cuando se verifica la herencia completa, entonces debe ser instancia de RuntimeException")
    void givenInvalidPriceException_whenCheckingFullInheritance_thenShouldBeRuntimeException() {
        // Given
        String errorMessage = "Invalid price value";

        // When
        InvalidPriceException exception = new InvalidPriceException(errorMessage);

        // Then
        assertInstanceOf(RuntimeException.class, exception);
    }

    @Test
    @DisplayName("Dado una InvalidPriceException, cuando se obtiene el stack trace, entonces debe estar disponible para depuración")
    void givenInvalidPriceException_whenGettingStackTrace_thenShouldBeAvailableForDebugging() {
        // Given
        String errorMessage = "Price validation failed";

        // When
        InvalidPriceException exception = new InvalidPriceException(errorMessage);

        // Then
        assertNotNull(exception.getStackTrace());
        assertTrue(exception.getStackTrace().length > 0);
    }

    @Test
    @DisplayName("Dado mensajes de error específicos de negocio, cuando se crea InvalidPriceException, entonces debe aceptar cualquier mensaje personalizado")
    void givenBusinessSpecificErrorMessages_whenCreatingException_thenShouldAcceptAnyCustomMessage() {
        // Given
        String discountError = "Discount price cannot be higher than original price";
        String currencyError = "Currency not supported for this market";
        String taxError = "Tax inclusive price calculation error";

        // When
        InvalidPriceException discountException = new InvalidPriceException(discountError);
        InvalidPriceException currencyException = new InvalidPriceException(currencyError);
        InvalidPriceException taxException = new InvalidPriceException(taxError);

        // Then
        assertEquals(discountError, discountException.getMessage());
        assertEquals(currencyError, currencyException.getMessage());
        assertEquals(taxError, taxException.getMessage());
    }

    @Test
    @DisplayName("Dado mensajes de error con formatos específicos, cuando se crea InvalidPriceException, entonces debe preservar el formato exacto")
    void givenFormattedErrorMessages_whenCreatingException_thenShouldPreserveExactFormat() {
        // Given
        String formattedMessage = "Price must be between $10.00 and $1000.00 (received: $-5.50)";
        String detailedMessage = "Invalid price: 15.999. Expected format: 15999 or 159.99";

        // When
        InvalidPriceException formattedException = new InvalidPriceException(formattedMessage);
        InvalidPriceException detailedException = new InvalidPriceException(detailedMessage);

        // Then
        assertEquals(formattedMessage, formattedException.getMessage());
        assertEquals(detailedMessage, detailedException.getMessage());
    }
}