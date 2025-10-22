package com.linktic.challenge.products.unit.domain.exception.valueobject;

import com.linktic.challenge.products.domain.exception.valueobject.InvalidProductDescriptionException;
import com.linktic.challenge.products.domain.exception.valueobject.ProductValueObjectException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InvalidProductDescriptionExceptionTest {

    @Test
    @DisplayName("Dado un mensaje de descripción inválida, cuando se crea InvalidProductDescriptionException, entonces la excepción debe contener el mensaje proporcionado")
    void givenDescriptionErrorMessage_whenCreatingException_thenShouldContainProvidedMessage() {
        // Given
        String expectedMessage = "Product description cannot be empty";

        // When
        InvalidProductDescriptionException exception = new InvalidProductDescriptionException(expectedMessage);

        // Then
        assertEquals(expectedMessage, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    @DisplayName("Dado diferentes mensajes de error de descripción, cuando se crea InvalidProductDescriptionException, entonces cada excepción debe contener su mensaje respectivo")
    void givenDifferentDescriptionErrorMessages_whenCreatingExceptions_thenEachShouldContainRespectiveMessage() {
        // Given
        String message1 = "Description exceeds maximum length of 1000 characters";
        String message2 = "Description contains invalid HTML tags";
        String message3 = "Description must be in supported language";

        // When
        InvalidProductDescriptionException exception1 = new InvalidProductDescriptionException(message1);
        InvalidProductDescriptionException exception2 = new InvalidProductDescriptionException(message2);
        InvalidProductDescriptionException exception3 = new InvalidProductDescriptionException(message3);

        // Then
        assertEquals(message1, exception1.getMessage());
        assertEquals(message2, exception2.getMessage());
        assertEquals(message3, exception3.getMessage());
    }

    @Test
    @DisplayName("Dado un mensaje de error vacío, cuando se crea InvalidProductDescriptionException, entonces la excepción debe manejar correctamente el mensaje vacío")
    void givenEmptyErrorMessage_whenCreatingException_thenShouldHandleEmptyMessageCorrectly() {
        // Given
        String emptyMessage = "";

        // When
        InvalidProductDescriptionException exception = new InvalidProductDescriptionException(emptyMessage);

        // Then
        assertEquals(emptyMessage, exception.getMessage());
    }

    @Test
    @DisplayName("Dado un mensaje de error nulo, cuando se crea InvalidProductDescriptionException, entonces la excepción debe manejar correctamente el mensaje nulo")
    void givenNullErrorMessage_whenCreatingException_thenShouldHandleNullMessageCorrectly() {

        // When
        InvalidProductDescriptionException exception = new InvalidProductDescriptionException(null);

        // Then
        assertNull(exception.getMessage());
    }

    @Test
    @DisplayName("Dado una InvalidProductDescriptionException, cuando se verifica la herencia, entonces debe ser instancia de ProductValueObjectException")
    void givenInvalidProductDescriptionException_whenCheckingInheritance_thenShouldBeProductValueObjectException() {
        // Given
        String errorMessage = "Invalid product description";

        // When
        InvalidProductDescriptionException exception = new InvalidProductDescriptionException(errorMessage);

        // Then
        assertInstanceOf(ProductValueObjectException.class, exception);
    }

    @Test
    @DisplayName("Dado una InvalidProductDescriptionException, cuando se verifica la herencia completa, entonces debe ser instancia de RuntimeException")
    void givenInvalidProductDescriptionException_whenCheckingFullInheritance_thenShouldBeRuntimeException() {
        // Given
        String errorMessage = "Invalid product description";

        // When
        InvalidProductDescriptionException exception = new InvalidProductDescriptionException(errorMessage);

        // Then
        assertInstanceOf(RuntimeException.class, exception);
    }

    @Test
    @DisplayName("Dado una InvalidProductDescriptionException, cuando se obtiene el stack trace, entonces debe estar disponible para depuración")
    void givenInvalidProductDescriptionException_whenGettingStackTrace_thenShouldBeAvailableForDebugging() {
        // Given
        String errorMessage = "Description validation failed";

        // When
        InvalidProductDescriptionException exception = new InvalidProductDescriptionException(errorMessage);

        // Then
        assertNotNull(exception.getStackTrace());
        assertTrue(exception.getStackTrace().length > 0);
    }

    @Test
    @DisplayName("Dado mensajes de error específicos de longitud, cuando se crea InvalidProductDescriptionException, entonces debe aceptar mensajes de longitud personalizados")
    void givenLengthSpecificErrorMessages_whenCreatingException_thenShouldAcceptCustomLengthMessages() {
        // Given
        String lengthMessage1 = "Description too short: 5 characters, minimum required: 10";
        String lengthMessage2 = "Description too long: 1500 characters, maximum allowed: 1000";
        String lengthMessage3 = "Description word count exceeds limit: 250 words, maximum: 200";

        // When
        InvalidProductDescriptionException exception1 = new InvalidProductDescriptionException(lengthMessage1);
        InvalidProductDescriptionException exception2 = new InvalidProductDescriptionException(lengthMessage2);
        InvalidProductDescriptionException exception3 = new InvalidProductDescriptionException(lengthMessage3);

        // Then
        assertEquals(lengthMessage1, exception1.getMessage());
        assertEquals(lengthMessage2, exception2.getMessage());
        assertEquals(lengthMessage3, exception3.getMessage());
    }

    @Test
    @DisplayName("Dado mensajes de error de contenido, cuando se crea InvalidProductDescriptionException, entonces debe manejar validaciones de contenido")
    void givenContentErrorMessages_whenCreatingException_thenShouldHandleContentValidations() {
        // Given
        String contentMessage1 = "Description contains prohibited words: [cheap, free, best]";
        String contentMessage2 = "Description includes invalid contact information";
        String contentMessage3 = "Description has insufficient product details";

        // When
        InvalidProductDescriptionException exception1 = new InvalidProductDescriptionException(contentMessage1);
        InvalidProductDescriptionException exception2 = new InvalidProductDescriptionException(contentMessage2);
        InvalidProductDescriptionException exception3 = new InvalidProductDescriptionException(contentMessage3);

        // Then
        assertEquals(contentMessage1, exception1.getMessage());
        assertEquals(contentMessage2, exception2.getMessage());
        assertEquals(contentMessage3, exception3.getMessage());
    }

    @Test
    @DisplayName("Dado mensajes de error de formato, cuando se crea InvalidProductDescriptionException, entonces debe manejar validaciones de formato")
    void givenFormatErrorMessages_whenCreatingException_thenShouldHandleFormatValidations() {
        // Given
        String formatMessage1 = "Description contains invalid HTML: <script> tags not allowed";
        String formatMessage2 = "Description has malformed Markdown syntax";
        String formatMessage3 = "Description includes broken URLs or invalid links";

        // When
        InvalidProductDescriptionException exception1 = new InvalidProductDescriptionException(formatMessage1);
        InvalidProductDescriptionException exception2 = new InvalidProductDescriptionException(formatMessage2);
        InvalidProductDescriptionException exception3 = new InvalidProductDescriptionException(formatMessage3);

        // Then
        assertEquals(formatMessage1, exception1.getMessage());
        assertEquals(formatMessage2, exception2.getMessage());
        assertEquals(formatMessage3, exception3.getMessage());
    }

    @Test
    @DisplayName("Dado mensajes de error con ejemplos específicos, cuando se crea InvalidProductDescriptionException, entonces debe preservar los ejemplos en el mensaje")
    void givenErrorMessagesWithExamples_whenCreatingException_thenShouldPreserveExamples() {
        // Given
        String exampleMessage = "Description must include key features. Example: 'This product features...'";
        String comparisonMessage = "Current description: 'Good product'. Suggested: 'High-quality product with...'";

        // When
        InvalidProductDescriptionException exampleException = new InvalidProductDescriptionException(exampleMessage);
        InvalidProductDescriptionException comparisonException = new InvalidProductDescriptionException(comparisonMessage);

        // Then
        assertEquals(exampleMessage, exampleException.getMessage());
        assertEquals(comparisonMessage, comparisonException.getMessage());
    }

    @Test
    @DisplayName("Dado mensajes de error de localización, cuando se crea InvalidProductDescriptionException, entonces debe manejar validaciones de idioma")
    void givenLocalizationErrorMessages_whenCreatingException_thenShouldHandleLanguageValidations() {
        // Given
        String languageMessage1 = "Description contains mixed languages, use only Spanish";
        String languageMessage2 = "Description has grammar or spelling errors";
        String languageMessage3 = "Description missing required translation for English market";

        // When
        InvalidProductDescriptionException exception1 = new InvalidProductDescriptionException(languageMessage1);
        InvalidProductDescriptionException exception2 = new InvalidProductDescriptionException(languageMessage2);
        InvalidProductDescriptionException exception3 = new InvalidProductDescriptionException(languageMessage3);

        // Then
        assertEquals(languageMessage1, exception1.getMessage());
        assertEquals(languageMessage2, exception2.getMessage());
        assertEquals(languageMessage3, exception3.getMessage());
    }

    @Test
    @DisplayName("Dado mensajes de error de SEO, cuando se crea InvalidProductDescriptionException, entonces debe manejar validaciones de optimización")
    void givenSeoErrorMessages_whenCreatingException_thenShouldHandleSeoValidations() {
        // Given
        String seoMessage1 = "Description missing primary keywords: [wireless, bluetooth, headphones]";
        String seoMessage2 = "Description keyword density too low for 'gaming laptop'";
        String seoMessage3 = "Description meta description exceeds 160 characters";

        // When
        InvalidProductDescriptionException exception1 = new InvalidProductDescriptionException(seoMessage1);
        InvalidProductDescriptionException exception2 = new InvalidProductDescriptionException(seoMessage2);
        InvalidProductDescriptionException exception3 = new InvalidProductDescriptionException(seoMessage3);

        // Then
        assertEquals(seoMessage1, exception1.getMessage());
        assertEquals(seoMessage2, exception2.getMessage());
        assertEquals(seoMessage3, exception3.getMessage());
    }
}