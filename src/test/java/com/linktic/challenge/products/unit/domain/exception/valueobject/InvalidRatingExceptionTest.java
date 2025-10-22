package com.linktic.challenge.products.unit.domain.exception.valueobject;

import com.linktic.challenge.products.domain.exception.valueobject.InvalidRatingException;
import com.linktic.challenge.products.domain.exception.valueobject.ProductValueObjectException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InvalidRatingExceptionTest {

    @Test
    @DisplayName("Dado un mensaje detallado de rating inválido, cuando se crea InvalidRatingException, entonces la excepción debe contener el mensaje correcto con el detalle")
    void givenDetailedRatingMessage_whenCreatingException_thenShouldContainCorrectMessageWithDetail() {
        // Given
        String detailedMessage = "Rating must be between 1 and 5";

        // When
        InvalidRatingException exception = new InvalidRatingException(detailedMessage);

        // Then
        assertEquals("Invalid rating: " + detailedMessage, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    @DisplayName("Dado diferentes mensajes detallados de rating inválido, cuando se crea InvalidRatingException, entonces cada excepción debe contener su respectivo detalle en el mensaje")
    void givenDifferentDetailedRatingMessages_whenCreatingExceptions_thenEachShouldContainRespectiveDetailInMessage() {
        // Given
        String detail1 = "Rating cannot be negative";
        String detail2 = "Rating exceeds maximum value of 5";
        String detail3 = "Rating must be a whole number";

        // When
        InvalidRatingException exception1 = new InvalidRatingException(detail1);
        InvalidRatingException exception2 = new InvalidRatingException(detail2);
        InvalidRatingException exception3 = new InvalidRatingException(detail3);

        // Then
        assertEquals("Invalid rating: " + detail1, exception1.getMessage());
        assertEquals("Invalid rating: " + detail2, exception2.getMessage());
        assertEquals("Invalid rating: " + detail3, exception3.getMessage());
    }

    @Test
    @DisplayName("Dado un mensaje detallado vacío, cuando se crea InvalidRatingException, entonces la excepción debe manejar correctamente el mensaje vacío")
    void givenEmptyDetailedMessage_whenCreatingException_thenShouldHandleEmptyMessageCorrectly() {
        // Given
        String emptyMessage = "";

        // When
        InvalidRatingException exception = new InvalidRatingException(emptyMessage);

        // Then
        assertEquals("Invalid rating: " + emptyMessage, exception.getMessage());
    }

    @Test
    @DisplayName("Dado un mensaje detallado nulo, cuando se crea InvalidRatingException, entonces la excepción debe manejar correctamente el mensaje nulo")
    void givenNullDetailedMessage_whenCreatingException_thenShouldHandleNullMessageCorrectly() {
        // When
        InvalidRatingException exception = new InvalidRatingException(null);

        // Then
        assertEquals("Invalid rating: null", exception.getMessage());
    }

    @Test
    @DisplayName("Dado una InvalidRatingException, cuando se verifica la herencia, entonces debe ser instancia de ProductValueObjectException")
    void givenInvalidRatingException_whenCheckingInheritance_thenShouldBeProductValueObjectException() {
        // Given
        String detailedMessage = "Rating validation failed";

        // When
        InvalidRatingException exception = new InvalidRatingException(detailedMessage);

        // Then
        assertInstanceOf(ProductValueObjectException.class, exception);
    }

    @Test
    @DisplayName("Dado una InvalidRatingException, cuando se verifica la herencia completa, entonces debe ser instancia de RuntimeException")
    void givenInvalidRatingException_whenCheckingFullInheritance_thenShouldBeRuntimeException() {
        // Given
        String detailedMessage = "Rating validation failed";

        // When
        InvalidRatingException exception = new InvalidRatingException(detailedMessage);

        // Then
        assertInstanceOf(RuntimeException.class, exception);
    }

    @Test
    @DisplayName("Dado una InvalidRatingException, cuando se obtiene el stack trace, entonces debe estar disponible para depuración")
    void givenInvalidRatingException_whenGettingStackTrace_thenShouldBeAvailableForDebugging() {
        // Given
        String detailedMessage = "Debug rating error";

        // When
        InvalidRatingException exception = new InvalidRatingException(detailedMessage);

        // Then
        assertNotNull(exception.getStackTrace());
        assertTrue(exception.getStackTrace().length > 0);
    }

    @Test
    @DisplayName("Dado mensajes detallados con valores específicos, cuando se crea InvalidRatingException, entonces debe incluir los valores en el mensaje")
    void givenDetailedMessagesWithSpecificValues_whenCreatingException_thenShouldIncludeValuesInMessage() {
        // Given
        String detail1 = "Received rating: -1.5, expected between 1.0 and 5.0";
        String detail2 = "Rating 6.7 exceeds maximum allowed value of 5.0";
        String detail3 = "Invalid decimal rating: 3.75, only whole numbers allowed";

        // When
        InvalidRatingException exception1 = new InvalidRatingException(detail1);
        InvalidRatingException exception2 = new InvalidRatingException(detail2);
        InvalidRatingException exception3 = new InvalidRatingException(detail3);

        // Then
        assertEquals("Invalid rating: " + detail1, exception1.getMessage());
        assertEquals("Invalid rating: " + detail2, exception2.getMessage());
        assertEquals("Invalid rating: " + detail3, exception3.getMessage());
    }

    @Test
    @DisplayName("Dado mensajes detallados sobre tipos de datos, cuando se crea InvalidRatingException, entonces debe manejar correctamente los tipos de datos")
    void givenDetailedMessagesAboutDataTypes_whenCreatingException_thenShouldHandleDataTypesCorrectly() {
        // Given
        String typeDetail1 = "Rating must be a numeric value, received: 'excellent'";
        String typeDetail2 = "Rating cannot be null";
        String typeDetail3 = "Rating must be a decimal number with maximum 2 decimal places";

        // When
        InvalidRatingException exception1 = new InvalidRatingException(typeDetail1);
        InvalidRatingException exception2 = new InvalidRatingException(typeDetail2);
        InvalidRatingException exception3 = new InvalidRatingException(typeDetail3);

        // Then
        assertEquals("Invalid rating: " + typeDetail1, exception1.getMessage());
        assertEquals("Invalid rating: " + typeDetail2, exception2.getMessage());
        assertEquals("Invalid rating: " + typeDetail3, exception3.getMessage());
    }

    @Test
    @DisplayName("Dado mensajes detallados con reglas de negocio específicas, cuando se crea InvalidRatingException, entonces debe aceptar cualquier regla de negocio")
    void givenBusinessRuleDetailedMessages_whenCreatingException_thenShouldAcceptAnyBusinessRule() {
        // Given
        String businessRule1 = "New products cannot have rating higher than 3";
        String businessRule2 = "Rating cannot be changed more than 2 points at once";
        String businessRule3 = "Minimum 10 reviews required for average rating calculation";

        // When
        InvalidRatingException exception1 = new InvalidRatingException(businessRule1);
        InvalidRatingException exception2 = new InvalidRatingException(businessRule2);
        InvalidRatingException exception3 = new InvalidRatingException(businessRule3);

        // Then
        assertEquals("Invalid rating: " + businessRule1, exception1.getMessage());
        assertEquals("Invalid rating: " + businessRule2, exception2.getMessage());
        assertEquals("Invalid rating: " + businessRule3, exception3.getMessage());
    }

    @Test
    @DisplayName("Dado mensajes detallados con contextos específicos, cuando se crea InvalidRatingException, entonces debe preservar el contexto completo")
    void givenContextSpecificDetailedMessages_whenCreatingException_thenShouldPreserveCompleteContext() {
        // Given
        String contextDetail1 = "User rating: 0, product: PROD-123, user: USR-456";
        String contextDetail2 = "Average rating calculation failed for product: PROD-789 - insufficient reviews";
        String contextDetail3 = "Rating update rejected - from 4.5 to 1.0 exceeds maximum change delta";

        // When
        InvalidRatingException exception1 = new InvalidRatingException(contextDetail1);
        InvalidRatingException exception2 = new InvalidRatingException(contextDetail2);
        InvalidRatingException exception3 = new InvalidRatingException(contextDetail3);

        // Then
        assertEquals("Invalid rating: " + contextDetail1, exception1.getMessage());
        assertEquals("Invalid rating: " + contextDetail2, exception2.getMessage());
        assertEquals("Invalid rating: " + contextDetail3, exception3.getMessage());
    }
}