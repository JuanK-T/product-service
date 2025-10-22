package com.linktic.challenge.products.unit.domain.model;

import com.linktic.challenge.products.domain.exception.valueobject.InvalidProductDescriptionException;
import com.linktic.challenge.products.domain.model.ProductDescription;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class ProductDescriptionTest {

    private static final int MIN_LENGTH = 10;
    private static final int MAX_LENGTH = 500;

    @Test
    @DisplayName("Dado una descripción válida, cuando se crea ProductDescription, entonces debe crearse correctamente")
    void givenValidDescription_whenCreatingProductDescription_thenShouldCreateSuccessfully() {
        // Given
        String validDescription = "Smartphone flagship con cámara profesional de 108MP y batería de larga duración";

        // When
        ProductDescription description = new ProductDescription(validDescription);

        // Then
        assertEquals(validDescription, description.value());
    }

    @Test
    @DisplayName("Dado descripción con longitud mínima (10 caracteres), cuando se crea ProductDescription, entonces debe crearse correctamente")
    void givenDescriptionWithMinimumLength_whenCreatingProductDescription_thenShouldCreateSuccessfully() {
        // Given
        String minLengthDescription = "A".repeat(MIN_LENGTH);

        // When
        ProductDescription description = new ProductDescription(minLengthDescription);

        // Then
        assertEquals(minLengthDescription, description.value());
        assertEquals(MIN_LENGTH, description.value().length());
    }

    @Test
    @DisplayName("Dado descripción con longitud máxima (500 caracteres), cuando se crea ProductDescription, entonces debe crearse correctamente")
    void givenDescriptionWithMaximumLength_whenCreatingProductDescription_thenShouldCreateSuccessfully() {
        // Given
        String maxLengthDescription = "Producto premium con características avanzadas. " + "A".repeat(450);

        // When
        ProductDescription description = new ProductDescription(maxLengthDescription);

        // Then
        assertEquals(maxLengthDescription, description.value());
        assertTrue(description.value().length() <= MAX_LENGTH);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("Dado descripción nula o vacía, cuando se crea ProductDescription, entonces debe lanzar InvalidProductDescriptionException")
    void givenNullOrEmptyDescription_whenCreatingProductDescription_thenShouldThrowInvalidProductDescriptionException(String invalidDescription) {
        // When & Then
        InvalidProductDescriptionException exception = assertThrows(InvalidProductDescriptionException.class, () ->
                new ProductDescription(invalidDescription)
        );

        if (invalidDescription == null) {
            assertEquals("Product description cannot be null", exception.getMessage());
        } else {
            assertEquals("Product description cannot be blank", exception.getMessage());
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {" ", "   ", "\t", "\n", "\r\n"})
    @DisplayName("Dado descripción con solo espacios en blanco, cuando se crea ProductDescription, entonces debe lanzar InvalidProductDescriptionException")
    void givenBlankDescription_whenCreatingProductDescription_thenShouldThrowInvalidProductDescriptionException(String blankDescription) {
        // When & Then
        InvalidProductDescriptionException exception = assertThrows(InvalidProductDescriptionException.class, () ->
                new ProductDescription(blankDescription)
        );

        assertEquals("Product description cannot be blank", exception.getMessage());
    }

    @Test
    @DisplayName("Dado descripción con menos de 10 caracteres, cuando se crea ProductDescription, entonces debe lanzar InvalidProductDescriptionException")
    void givenDescriptionShorterThanMinimum_whenCreatingProductDescription_thenShouldThrowInvalidProductDescriptionException() {
        // Given
        String shortDescription = "Muy corto";

        // When & Then
        InvalidProductDescriptionException exception = assertThrows(InvalidProductDescriptionException.class, () ->
                new ProductDescription(shortDescription)
        );

        assertEquals("Product description must be at least " + MIN_LENGTH + " characters", exception.getMessage());
    }

    @Test
    @DisplayName("Dado descripción que excede 500 caracteres, cuando se crea ProductDescription, entonces debe lanzar InvalidProductDescriptionException")
    void givenDescriptionExceedingMaximum_whenCreatingProductDescription_thenShouldThrowInvalidProductDescriptionException() {
        // Given
        String longDescription = "Producto extremadamente detallado con especificaciones técnicas completas. " +
                "Este producto incluye todas las características avanzadas que puedas imaginar. " +
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. ".repeat(20);

        // When & Then
        InvalidProductDescriptionException exception = assertThrows(InvalidProductDescriptionException.class, () ->
                new ProductDescription(longDescription)
        );

        assertEquals("Product description cannot exceed " + MAX_LENGTH + " characters", exception.getMessage());
    }

    @Test
    @DisplayName("Dado dos ProductDescription con el mismo valor, cuando se comparan, entonces deben ser iguales")
    void givenTwoProductDescriptionsWithSameValue_whenComparing_thenShouldBeEqual() {
        // Given
        String desc = "Laptop profesional para trabajo intensivo con procesador Intel Core i7";
        ProductDescription description1 = new ProductDescription(desc);
        ProductDescription description2 = new ProductDescription(desc);

        // Then
        assertEquals(description1, description2);
        assertEquals(description1.hashCode(), description2.hashCode());
    }

    @Test
    @DisplayName("Dado dos ProductDescription con valores diferentes, cuando se comparan, entonces no deben ser iguales")
    void givenTwoProductDescriptionsWithDifferentValues_whenComparing_thenShouldNotBeEqual() {
        // Given
        ProductDescription description1 = new ProductDescription("Smartphone con cámara avanzada");
        ProductDescription description2 = new ProductDescription("Laptop para gaming profesional");

        // Then
        assertNotEquals(description1, description2);
    }

    @Test
    @DisplayName("Dado un ProductDescription, cuando se obtiene su representación string, entonces debe incluir el valor")
    void givenProductDescription_whenCallingToString_thenShouldIncludeValue() {
        // Given
        String desc = "Tablet con pantalla retina y stylus incluido";
        ProductDescription description = new ProductDescription(desc);

        // When
        String stringRepresentation = description.toString();

        // Then
        assertTrue(stringRepresentation.contains(desc));
    }

    @ParameterizedTest
    @MethodSource("lengthBoundaryDescriptionsProvider")
    @DisplayName("Dado descripciones en los límites de longitud, cuando se crea ProductDescription, entonces deben manejarse correctamente")
    void givenLengthBoundaryDescriptions_whenCreatingProductDescription_thenShouldHandleCorrectly(String description, boolean shouldBeValid, String expectedError) {
        if (shouldBeValid) {
            assertDoesNotThrow(() -> {
                ProductDescription productDescription = new ProductDescription(description);
                assertEquals(description, productDescription.value());
            });
        } else {
            InvalidProductDescriptionException exception = assertThrows(InvalidProductDescriptionException.class, () ->
                    new ProductDescription(description)
            );
            if (expectedError != null) {
                assertEquals(expectedError, exception.getMessage());
            }
        }
    }

    static Stream<Arguments> lengthBoundaryDescriptionsProvider() {
        return Stream.of(
                Arguments.of("", false, "Product description cannot be blank"),
                Arguments.of("Muy corto", false, "Product description must be at least " + MIN_LENGTH + " characters"),
                Arguments.of("Exactamente10", true, null), // 13 caracteres
                Arguments.of("A".repeat(MIN_LENGTH), true, null),
                Arguments.of("A".repeat(MAX_LENGTH), true, null),
                Arguments.of("A".repeat(MAX_LENGTH + 1), false, "Product description cannot exceed " + MAX_LENGTH + " characters")
        );
    }

    @Test
    @DisplayName("Dado descripciones realistas de e-commerce, cuando se crea ProductDescription, entonces deben crearse correctamente")
    void givenRealisticEcommerceDescriptions_whenCreatingProductDescription_thenShouldCreateSuccessfully() {
        // Given
        String[] realisticDescriptions = {
                "Smartphone flagship con cámara triple de 108MP, pantalla AMOLED de 6.7\" y batería de 5000mAh.",
                "Laptop gaming con RTX 4060, procesador Intel i9, 32GB RAM y SSD 1TB. Ideal para juegos y streaming.",
                "Audífonos noise cancelling con 30h de batería, carga rápida y sonido surround inmersivo.",
                "Smart TV 4K 55\" con Android TV, HDR10+ y asistente de voz integrado. Perfecta para entretenimiento.",
                "Tablet digitalizadora con lápiz activo, ideal para artistas y diseñadores profesionales."
        };

        // When & Then
        for (String desc : realisticDescriptions) {
            assertDoesNotThrow(() -> {
                ProductDescription description = new ProductDescription(desc);
                assertEquals(desc, description.value());
                assertTrue(description.value().length() >= MIN_LENGTH, "Description should meet minimum length: " + desc);
                assertTrue(description.value().length() <= MAX_LENGTH, "Description should not exceed maximum length: " + desc);
            }, "Should accept realistic description: " + desc);
        }
    }

    @Test
    @DisplayName("Dado descripciones con caracteres especiales, cuando se crea ProductDescription, entonces deben crearse correctamente")
    void givenDescriptionsWithSpecialCharacters_whenCreatingProductDescription_thenShouldCreateSuccessfully() {
        // Given
        String[] specialCharDescriptions = {
                "Cafetera automática con molino intégré - ¡Café recién molido!",
                "Producto 100% original garantizado + 2 años de warranty.",
                "Router Wi-Fi 6 (AX1800) con 4 antenas & puerto Gigabit.",
                "Monitor 27\" QHD @144Hz - Color accuracy: ΔE < 2.",
                "Teclado mecánico RGB con switches Blue — Click sonoro."
        };

        // When & Then
        for (String desc : specialCharDescriptions) {
            assertDoesNotThrow(() -> {
                ProductDescription description = new ProductDescription(desc);
                assertEquals(desc, description.value());
            }, "Should accept description with special characters: " + desc);
        }
    }

    @Test
    @DisplayName("Dado descripciones con caracteres internacionales, cuando se crea ProductDescription, entonces deben crearse correctamente")
    void givenDescriptionsWithInternationalCharacters_whenCreatingProductDescription_thenShouldCreateSuccessfully() {
        // Given
        String[] internationalDescriptions = {
                "Auriculares inalámbricos con cancelación activa de ruido y calidad de sonido excepcional.",
                "スマートフォン：高性能カメラ搭載、長時間バッテリー持ち", // Japonés
                "Наушники с шумоподавлением и премиальным звуком", // Ruso
                "Café molido 100% arábica - tueste medio para sabor equilibrado"
        };

        // When & Then
        for (String desc : internationalDescriptions) {
            assertDoesNotThrow(() -> {
                ProductDescription description = new ProductDescription(desc);
                assertEquals(desc, description.value());
            }, "Should accept international description: " + desc);
        }
    }

    @Test
    @DisplayName("Dado descripción con números y especificaciones técnicas, cuando se crea ProductDescription, entonces debe crearse correctamente")
    void givenDescriptionWithNumbersAndSpecs_whenCreatingProductDescription_thenShouldCreateSuccessfully() {
        // Given
        String technicalDescription = "Procesador Snapdragon 8 Gen 2, 12GB RAM, 256GB almacenamiento, " +
                "pantalla 6.8\" Dynamic AMOLED 2X, cámara 200MP + 50MP + 12MP, " +
                "batería 5000mAh con carga rápida de 45W.";

        // When
        ProductDescription description = new ProductDescription(technicalDescription);

        // Then
        assertEquals(technicalDescription, description.value());
        assertTrue(description.value().contains("Snapdragon"));
        assertTrue(description.value().contains("200MP"));
    }

    @Test
    @DisplayName("Dado comparación con null y otro tipo, cuando se verifica equals, entonces debe retornar false")
    void givenNullAndDifferentType_whenCheckingEquals_thenShouldReturnFalse() {
        // Given
        ProductDescription description = new ProductDescription("Producto de alta calidad");

        // Then
        assertNotEquals(null, description, "Equals con null debe retornar false");
    }

    @Test
    @DisplayName("Dado descripciones con diferentes formatos, cuando se crea ProductDescription, entonces deben validarse correctamente")
    void givenDescriptionsWithDifferentFormats_whenCreatingProductDescription_thenShouldValidateCorrectly() {
        // Given
        String bulletPoints = "• Pantalla 6.7\" Super Retina XDR\n• Chip A16 Bionic\n• Cámara 48MP\n• iOS 16";
        String withEmojis = "🚀 Rendimiento máximo 🎮 Ideal para gaming 💾 Almacenamiento rápido";
        String withUrls = "Visita nuestro sitio web para más especificaciones técnicas completas";

        // When & Then
        assertDoesNotThrow(() -> new ProductDescription(bulletPoints));
        assertDoesNotThrow(() -> new ProductDescription(withEmojis));
        assertDoesNotThrow(() -> new ProductDescription(withUrls));
    }

    @Test
    @DisplayName("Dado descripción justo en el límite inferior, cuando se crea ProductDescription, entonces debe crearse correctamente")
    void givenDescriptionAtLowerBoundary_whenCreatingProductDescription_thenShouldCreateSuccessfully() {
        // Given
        String boundaryDescription = "Prod de 10"; // Exactly 10 characters

        // When
        ProductDescription description = new ProductDescription(boundaryDescription);

        // Then
        assertEquals(boundaryDescription, description.value());
        assertEquals(MIN_LENGTH, description.value().length());
    }

    @Test
    @DisplayName("Dado descripción justo en el límite superior, cuando se crea ProductDescription, entonces debe crearse correctamente")
    void givenDescriptionAtUpperBoundary_whenCreatingProductDescription_thenShouldCreateSuccessfully() {
        // Given
        String exact500 = "A".repeat(MAX_LENGTH); // Exactly 500 characters

        // When
        ProductDescription description = new ProductDescription(exact500);

        // Then
        assertEquals(exact500, description.value());
        assertEquals(MAX_LENGTH, description.value().length());
    }
}
