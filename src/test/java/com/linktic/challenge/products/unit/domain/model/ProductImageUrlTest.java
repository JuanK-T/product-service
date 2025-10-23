package com.linktic.challenge.products.unit.domain.model;

import com.linktic.challenge.products.domain.exception.valueobject.InvalidImageUrlException;
import com.linktic.challenge.products.domain.model.ProductImageUrl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class ProductImageUrlTest {

    private static final String DEFAULT_IMAGE = "https://st5.depositphotos.com/90358332/74974/v/450/depositphotos_749740000-stock-illustration-photo-thumbnail-graphic-element-found.jpg";

    @Test
    @DisplayName("Dado una URL HTTPS válida, cuando se crea ProductImageUrl, entonces debe crearse correctamente")
    void givenValidHttpsUrl_whenCreatingProductImageUrl_thenShouldCreateSuccessfully() {
        // Given
        String validUrl = "https://example.com/image.jpg";

        // When
        ProductImageUrl imageUrl = new ProductImageUrl(validUrl);

        // Then
        assertEquals(validUrl, imageUrl.value());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "https://example.com/product.jpg",
            "https://sub.domain.com/images/photo.png",
            "https://cdn.shopify.com/s/files/1/1234/5678/products/image.jpg",
            "https://example.com/image.png?width=300&height=300",
            "https://example.com/path/to/deep/image.gif"
    })
    @DisplayName("Dado diferentes URLs HTTPS válidas, cuando se crea ProductImageUrl, entonces deben crearse correctamente")
    void givenDifferentValidHttpsUrls_whenCreatingProductImageUrl_thenShouldCreateSuccessfully(String validUrl) {
        // When & Then
        assertDoesNotThrow(() -> {
            ProductImageUrl imageUrl = new ProductImageUrl(validUrl);
            assertEquals(validUrl, imageUrl.value());
        });
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "   ", "\t", "\n"})
    @DisplayName("Dado URL nula, vacía o en blanco, cuando se crea ProductImageUrl, entonces debe usar imagen por defecto")
    void givenNullOrBlankUrl_whenCreatingProductImageUrl_thenShouldUseDefaultImage(String invalidUrl) {
        // When
        ProductImageUrl imageUrl = new ProductImageUrl(invalidUrl);

        // Then
        assertEquals(DEFAULT_IMAGE, imageUrl.value());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "http://example.com/image.jpg",           // HTTP instead of HTTPS
            "ftp://example.com/image.jpg",            // FTP protocol
            "file:///path/to/image.jpg",              // File protocol
            "javascript:alert('xss')",                // JavaScript pseudo-protocol
            "data:image/png;base64,abc123",           // Data URI
            "//example.com/image.jpg",                // Protocol-relative URL
            "example.com/image.jpg",                  // No protocol
            "/relative/path/image.jpg",               // Relative path
            "invalid-url",                            // Invalid string
            "https://",                               // No host
            "https:/example.com",                     // Single slash
            "https:///example.com",                    // Triple slash
            "https://exa mple.com/image.jpg",        // ❗ Fuerza URISyntaxException (espacio en host)
            "https://example.com/image with space.jpg" // ❗ Fuerza URISyntaxException (espacio en path)
    })
    @DisplayName("Dado URLs inválidas o con protocolo no HTTPS, cuando se crea ProductImageUrl, entonces debe lanzar InvalidImageUrlException")
    void givenInvalidUrls_whenCreatingProductImageUrl_thenShouldThrowInvalidImageUrlException(String invalidUrl) {
        // When & Then
        InvalidImageUrlException exception = assertThrows(InvalidImageUrlException.class, () ->
                new ProductImageUrl(invalidUrl)
        );

        assertEquals("Invalid image URL: " + invalidUrl, exception.getMessage());
    }

    @Test
    @DisplayName("Dado URL con parámetros de consulta, cuando se crea ProductImageUrl, entonces debe crearse correctamente")
    void givenUrlWithQueryParameters_whenCreatingProductImageUrl_thenShouldCreateSuccessfully() {
        // Given
        String urlWithParams = "https://example.com/image.jpg?width=300&height=200&quality=85";

        // When
        ProductImageUrl imageUrl = new ProductImageUrl(urlWithParams);

        // Then
        assertEquals(urlWithParams, imageUrl.value());
    }

    @Test
    @DisplayName("Dado URL con fragmento, cuando se crea ProductImageUrl, entonces debe crearse correctamente")
    void givenUrlWithFragment_whenCreatingProductImageUrl_thenShouldCreateSuccessfully() {
        // Given
        String urlWithFragment = "https://example.com/image.jpg#section";

        // When
        ProductImageUrl imageUrl = new ProductImageUrl(urlWithFragment);

        // Then
        assertEquals(urlWithFragment, imageUrl.value());
    }

    @Test
    @DisplayName("Dado URL con puerto, cuando se crea ProductImageUrl, entonces debe crearse correctamente")
    void givenUrlWithPort_whenCreatingProductImageUrl_thenShouldCreateSuccessfully() {
        // Given
        String urlWithPort = "https://example.com:8080/image.jpg";

        // When
        ProductImageUrl imageUrl = new ProductImageUrl(urlWithPort);

        // Then
        assertEquals(urlWithPort, imageUrl.value());
    }

    @Test
    @DisplayName("Dado URL con autenticación, cuando se crea ProductImageUrl, entonces debe crearse correctamente")
    void givenUrlWithAuthentication_whenCreatingProductImageUrl_thenShouldCreateSuccessfully() {
        // Given
        String urlWithAuth = "https://user:pass@example.com/image.jpg";

        // When
        ProductImageUrl imageUrl = new ProductImageUrl(urlWithAuth);

        // Then
        assertEquals(urlWithAuth, imageUrl.value());
    }

    @Test
    @DisplayName("Dado parámetros válidos, cuando se usa factory method of(), entonces debe crear el mismo ProductImageUrl")
    void givenValidParameters_whenUsingFactoryMethod_thenShouldCreateSameProductImageUrl() {
        // Given
        String validUrl = "https://example.com/product.jpg";

        // When
        ProductImageUrl fromConstructor = new ProductImageUrl(validUrl);
        ProductImageUrl fromFactory = ProductImageUrl.of(validUrl);

        // Then
        assertEquals(fromConstructor.value(), fromFactory.value());
        assertEquals(validUrl, fromFactory.value());
    }

    @Test
    @DisplayName("Dado URL nula, cuando se usa factory method of(), entonces debe usar imagen por defecto")
    void givenNullUrl_whenUsingFactoryMethod_thenShouldUseDefaultImage() {
        // When
        ProductImageUrl imageUrl = ProductImageUrl.of(null);

        // Then
        assertEquals(DEFAULT_IMAGE, imageUrl.value());
    }

    @Test
    @DisplayName("Dado dos ProductImageUrl con el mismo valor, cuando se comparan, entonces deben ser iguales")
    void givenTwoProductImageUrlsWithSameValue_whenComparing_thenShouldBeEqual() {
        // Given
        String url = "https://example.com/image.jpg";
        ProductImageUrl imageUrl1 = new ProductImageUrl(url);
        ProductImageUrl imageUrl2 = new ProductImageUrl(url);

        // Then
        assertEquals(imageUrl1, imageUrl2);
        assertEquals(imageUrl1.hashCode(), imageUrl2.hashCode());
    }

    @Test
    @DisplayName("Dado dos ProductImageUrl con valores diferentes, cuando se comparan, entonces no deben ser iguales")
    void givenTwoProductImageUrlsWithDifferentValues_whenComparing_thenShouldNotBeEqual() {
        // Given
        ProductImageUrl imageUrl1 = new ProductImageUrl("https://example.com/image1.jpg");
        ProductImageUrl imageUrl2 = new ProductImageUrl("https://example.com/image2.jpg");

        // Then
        assertNotEquals(imageUrl1, imageUrl2);
    }

    @Test
    @DisplayName("Dado un ProductImageUrl, cuando se obtiene su representación string, entonces debe incluir el valor")
    void givenProductImageUrl_whenCallingToString_thenShouldIncludeValue() {
        // Given
        String url = "https://example.com/product.jpg";
        ProductImageUrl imageUrl = new ProductImageUrl(url);

        // When
        String stringRepresentation = imageUrl.toString();

        // Then
        assertTrue(stringRepresentation.contains(url));
    }

    @Test
    @DisplayName("Dado URL por defecto explícitamente, cuando se crea ProductImageUrl, entonces debe crearse correctamente")
    void givenDefaultUrlExplicitly_whenCreatingProductImageUrl_thenShouldCreateSuccessfully() {
        // When
        ProductImageUrl imageUrl = new ProductImageUrl(DEFAULT_IMAGE);

        // Then
        assertEquals(DEFAULT_IMAGE, imageUrl.value());
    }

    @ParameterizedTest
    @MethodSource("urlValidationProvider")
    @DisplayName("Dado diferentes casos de URLs, cuando se crea ProductImageUrl, entonces debe validar correctamente")
    void givenVariousUrlCases_whenCreatingProductImageUrl_thenShouldValidateCorrectly(String url, boolean shouldBeValid, String description) {
        if (shouldBeValid) {
            assertDoesNotThrow(() -> {
                ProductImageUrl imageUrl = new ProductImageUrl(url);
                assertNotNull(imageUrl.value());
            }, "Should be valid: " + description);
        } else {
            assertThrows(InvalidImageUrlException.class, () ->
                            new ProductImageUrl(url),
                    "Should be invalid: " + description
            );
        }
    }

    static Stream<Arguments> urlValidationProvider() {
        return Stream.of(
                Arguments.of("https://example.com", true, "Simple HTTPS URL"),
                Arguments.of("https://example.com/image.jpg", true, "HTTPS with path"),
                Arguments.of("http://example.com", false, "HTTP protocol"),
                Arguments.of(null, true, "Null URL (uses default)"),
                Arguments.of("", true, "Empty URL (uses default)"),
                Arguments.of("   ", true, "Blank URL (uses default)"),
                Arguments.of("https://", false, "HTTPS without host"),
                Arguments.of("https://example.com:443/image.jpg", true, "HTTPS with port"),
                Arguments.of("ftp://example.com/image.jpg", false, "FTP protocol"),
                Arguments.of("https://sub.domain.example.com/path/to/image.png", true, "Complex HTTPS URL")
        );
    }

    @Test
    @DisplayName("Dado URLs de CDN populares, cuando se crea ProductImageUrl, entonces deben crearse correctamente")
    void givenPopularCdnUrls_whenCreatingProductImageUrl_thenShouldCreateSuccessfully() {
        // Given
        String[] cdnUrls = {
                "https://images.unsplash.com/photo-1234567890",
                "https://cdn.shopify.com/s/files/1/2345/6789/products/image.jpg",
                "https://assets.example.com/images/product.jpg",
                "https://cloudfront.net/images/photo.png",
                "https://akamai.net/content/image.gif"
        };

        // When & Then
        for (String url : cdnUrls) {
            assertDoesNotThrow(() -> {
                ProductImageUrl imageUrl = new ProductImageUrl(url);
                assertEquals(url, imageUrl.value());
            }, "Should accept CDN URL: " + url);
        }
    }

    @Test
    @DisplayName("Dado URLs con caracteres internacionales, cuando se crea ProductImageUrl, entonces deben crearse correctamente")
    void givenUrlsWithInternationalCharacters_whenCreatingProductImageUrl_thenShouldCreateSuccessfully() {
        // Given
        String internationalUrl = "https://example.com/imágenes/producto.jpg";

        // When
        ProductImageUrl imageUrl = new ProductImageUrl(internationalUrl);

        // Then
        assertEquals(internationalUrl, imageUrl.value());
    }

    @Test
    @DisplayName("Dado comparación con null y otro tipo, cuando se verifica equals, entonces debe retornar false")
    void givenNullAndDifferentType_whenCheckingEquals_thenShouldReturnFalse() {
        // Given
        ProductImageUrl imageUrl = new ProductImageUrl("https://example.com/image.jpg");

        // Then
        assertNotEquals(null, imageUrl, "Equals con null debe retornar false");
    }
}
