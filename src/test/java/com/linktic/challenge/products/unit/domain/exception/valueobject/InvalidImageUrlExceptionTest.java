package com.linktic.challenge.products.unit.domain.exception.valueobject;

import com.linktic.challenge.products.domain.exception.valueobject.InvalidImageUrlException;
import com.linktic.challenge.products.domain.exception.valueobject.ProductValueObjectException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InvalidImageUrlExceptionTest {

    @Test
    @DisplayName("Dado una URL de imagen inválida, cuando se crea InvalidImageUrlException, entonces la excepción debe contener el mensaje correcto con la URL")
    void givenInvalidImageUrl_whenCreatingException_thenShouldContainCorrectMessageWithUrl() {
        // Given
        String url = "invalid-url";

        // When
        InvalidImageUrlException exception = new InvalidImageUrlException(url);

        // Then
        assertEquals("Invalid image URL: " + url, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    @DisplayName("Dado diferentes URLs de imagen inválidas, cuando se crea InvalidImageUrlException, entonces cada excepción debe contener su respectiva URL en el mensaje")
    void givenDifferentInvalidImageUrls_whenCreatingExceptions_thenEachShouldContainRespectiveUrlInMessage() {
        // Given
        String url1 = "ftp://example.com/image.jpg";
        String url2 = "not-a-url";
        String url3 = "http://";

        // When
        InvalidImageUrlException exception1 = new InvalidImageUrlException(url1);
        InvalidImageUrlException exception2 = new InvalidImageUrlException(url2);
        InvalidImageUrlException exception3 = new InvalidImageUrlException(url3);

        // Then
        assertEquals("Invalid image URL: " + url1, exception1.getMessage());
        assertEquals("Invalid image URL: " + url2, exception2.getMessage());
        assertEquals("Invalid image URL: " + url3, exception3.getMessage());
    }

    @Test
    @DisplayName("Dado una URL de imagen vacía, cuando se crea InvalidImageUrlException, entonces la excepción debe manejar correctamente la URL vacía")
    void givenEmptyImageUrl_whenCreatingException_thenShouldHandleEmptyUrlCorrectly() {
        // Given
        String emptyUrl = "";

        // When
        InvalidImageUrlException exception = new InvalidImageUrlException(emptyUrl);

        // Then
        assertEquals("Invalid image URL: " + emptyUrl, exception.getMessage());
    }

    @Test
    @DisplayName("Dado una URL de imagen nula, cuando se crea InvalidImageUrlException, entonces la excepción debe manejar correctamente la URL nula")
    void givenNullImageUrl_whenCreatingException_thenShouldHandleNullUrlCorrectly() {
        // When
        InvalidImageUrlException exception = new InvalidImageUrlException(null);

        // Then
        assertEquals("Invalid image URL: null", exception.getMessage());
    }

    @Test
    @DisplayName("Dado una InvalidImageUrlException, cuando se verifica la herencia, entonces debe ser instancia de ProductValueObjectException")
    void givenInvalidImageUrlException_whenCheckingInheritance_thenShouldBeProductValueObjectException() {
        // Given
        String url = "http://test.com/image.jpg";

        // When
        InvalidImageUrlException exception = new InvalidImageUrlException(url);

        // Then
        assertInstanceOf(ProductValueObjectException.class, exception);
    }

    @Test
    @DisplayName("Dado una InvalidImageUrlException, cuando se verifica la herencia completa, entonces debe ser instancia de RuntimeException")
    void givenInvalidImageUrlException_whenCheckingFullInheritance_thenShouldBeRuntimeException() {
        // Given
        String url = "http://test.com/image.jpg";

        // When
        InvalidImageUrlException exception = new InvalidImageUrlException(url);

        // Then
        assertInstanceOf(RuntimeException.class, exception);
    }

    @Test
    @DisplayName("Dado una InvalidImageUrlException, cuando se obtiene el stack trace, entonces debe estar disponible para depuración")
    void givenInvalidImageUrlException_whenGettingStackTrace_thenShouldBeAvailableForDebugging() {
        // Given
        String url = "debug-url";

        // When
        InvalidImageUrlException exception = new InvalidImageUrlException(url);

        // Then
        assertNotNull(exception.getStackTrace());
        assertTrue(exception.getStackTrace().length > 0);
    }

    @Test
    @DisplayName("Dado URLs de imagen con espacios, cuando se crea InvalidImageUrlException, entonces debe incluir los espacios en el mensaje")
    void givenImageUrlWithSpaces_whenCreatingException_thenShouldIncludeSpacesInMessage() {
        // Given
        String urlWithSpaces = "http://example.com/my image.jpg";

        // When
        InvalidImageUrlException exception = new InvalidImageUrlException(urlWithSpaces);

        // Then
        assertEquals("Invalid image URL: " + urlWithSpaces, exception.getMessage());
    }

    @Test
    @DisplayName("Dado URLs de imagen con caracteres especiales, cuando se crea InvalidImageUrlException, entonces debe incluir todos los caracteres en el mensaje")
    void givenImageUrlWithSpecialCharacters_whenCreatingException_thenShouldIncludeAllCharactersInMessage() {
        // Given
        String urlWithSpecialChars = "http://example.com/image@2x.jpg?width=800&height=600";

        // When
        InvalidImageUrlException exception = new InvalidImageUrlException(urlWithSpecialChars);

        // Then
        assertEquals("Invalid image URL: " + urlWithSpecialChars, exception.getMessage());
    }

    @Test
    @DisplayName("Dado URLs de imagen que exceden longitud máxima, cuando se crea InvalidImageUrlException, entonces debe manejar URLs largas correctamente")
    void givenLongImageUrl_whenCreatingException_thenShouldHandleLongUrlCorrectly() {
        // Given
        String longUrl = "https://example.com/images/products/" +
                "very-long-product-name-that-exceeds-normal-length/" +
                "high-resolution-image-with-many-details-and-special-characteristics.jpg" +
                "?version=1234567890&quality=high&format=webp";

        // When
        InvalidImageUrlException exception = new InvalidImageUrlException(longUrl);

        // Then
        assertEquals("Invalid image URL: " + longUrl, exception.getMessage());
    }
}