package com.linktic.challenge.products.unit.domain.model;

import com.linktic.challenge.products.domain.exception.valueobject.InvalidProductNameException;
import com.linktic.challenge.products.domain.model.ProductName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class ProductNameTest {

    @Test
    @DisplayName("Dado un nombre válido, cuando se crea ProductName, entonces debe crearse correctamente")
    void givenValidName_whenCreatingProductName_thenShouldCreateSuccessfully() {
        // Given
        String validName = "Smartphone Galaxy XZ";

        // When
        ProductName productName = new ProductName(validName);

        // Then
        assertEquals(validName, productName.value());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "Prod",      // Exact minimum length (4 characters)
            "Product",   // More than minimum
            "Product Name",
            "Product-Name",
            "Product_Name",
            "Product123",
            "Café",      // With special characters
            "产品名称测试", // International characters
            "Samsung Galaxy S23 Ultra 5G Edition" // Long but valid name
    })
    @DisplayName("Dado diferentes nombres válidos, cuando se crea ProductName, entonces deben crearse correctamente")
    void givenDifferentValidNames_whenCreatingProductName_thenShouldCreateSuccessfully(String validName) {
        // When & Then
        assertDoesNotThrow(() -> {
            ProductName productName = new ProductName(validName);
            assertEquals(validName, productName.value());
        });
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("Dado nombre nulo o vacío, cuando se crea ProductName, entonces debe lanzar InvalidProductNameException")
    void givenNullOrEmptyName_whenCreatingProductName_thenShouldThrowInvalidProductNameException(String invalidName) {
        // When & Then
        InvalidProductNameException exception = assertThrows(InvalidProductNameException.class, () ->
                new ProductName(invalidName)
        );

        if (invalidName == null) {
            assertEquals("Invalid product name: Product name cannot be null", exception.getMessage());
        } else {
            assertEquals("Invalid product name: Product name cannot be blank", exception.getMessage());
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {" ", "   ", "\t", "\n", "\r", "\r\n"})
    @DisplayName("Dado nombre con solo espacios en blanco, cuando se crea ProductName, entonces debe lanzar InvalidProductNameException")
    void givenBlankName_whenCreatingProductName_thenShouldThrowInvalidProductNameException(String blankName) {
        // When & Then
        InvalidProductNameException exception = assertThrows(InvalidProductNameException.class, () ->
                new ProductName(blankName)
        );

        assertEquals("Invalid product name: Product name cannot be blank", exception.getMessage());
    }

    @Test
    @DisplayName("Dado nombre con longitud máxima (60 caracteres), cuando se crea ProductName, entonces debe crearse correctamente")
    void givenNameWithMaximumLength_whenCreatingProductName_thenShouldCreateSuccessfully() {
        // Given
        String maxLengthName = "A".repeat(60);

        // When
        ProductName productName = new ProductName(maxLengthName);

        // Then
        assertEquals(maxLengthName, productName.value());
        assertEquals(60, productName.value().length());
    }

    @Test
    @DisplayName("Dado nombre que excede la longitud máxima, cuando se crea ProductName, entonces debe lanzar InvalidProductNameException")
    void givenNameExceedingMaximumLength_whenCreatingProductName_thenShouldThrowInvalidProductNameException() {
        // Given
        String tooLongName = "A".repeat(61);

        // When & Then
        InvalidProductNameException exception = assertThrows(InvalidProductNameException.class, () ->
                new ProductName(tooLongName)
        );

        assertEquals("Invalid product name: Product name cannot exceed 60 characters", exception.getMessage());
    }

    @Test
    @DisplayName("Dado nombre con longitud mínima (4 caracteres), cuando se crea ProductName, entonces debe crearse correctamente")
    void givenNameWithMinimumLength_whenCreatingProductName_thenShouldCreateSuccessfully() {
        // Given
        String minLengthName = "ABCD";

        // When
        ProductName productName = new ProductName(minLengthName);

        // Then
        assertEquals(minLengthName, productName.value());
        assertEquals(4, productName.value().length());
    }

    @Test
    @DisplayName("Dado nombre con 3 caracteres (menos del mínimo), cuando se crea ProductName, entonces debe lanzar InvalidProductNameException")
    void givenNameWithThreeCharacters_whenCreatingProductName_thenShouldThrowInvalidProductNameException() {
        // Given
        String shortName = "ABC";

        // When & Then
        InvalidProductNameException exception = assertThrows(InvalidProductNameException.class, () ->
                new ProductName(shortName)
        );

        assertEquals("Invalid product name: Product name must be at least 4 characters", exception.getMessage());
    }

    @Test
    @DisplayName("Dado nombre con espacios al inicio y final, cuando se crea ProductName, entonces debe crearse correctamente preservando los espacios")
    void givenNameWithLeadingAndTrailingSpaces_whenCreatingProductName_thenShouldCreateSuccessfully() {
        // Given
        String nameWithSpaces = "  Smartphone Galaxy  ";
        String expectedName = "  Smartphone Galaxy  ";

        // When
        ProductName productName = new ProductName(nameWithSpaces);

        // Then
        assertEquals(expectedName, productName.value());
        assertTrue(productName.value().length() >= 4, "Name with spaces should still meet minimum length");
    }

    @Test
    @DisplayName("Dado dos ProductName con el mismo valor, cuando se comparan, entonces deben ser iguales")
    void givenTwoProductNamesWithSameValue_whenComparing_thenShouldBeEqual() {
        // Given
        String name = "Laptop Pro Ultra";
        ProductName productName1 = new ProductName(name);
        ProductName productName2 = new ProductName(name);

        // Then
        assertEquals(productName1, productName2);
        assertEquals(productName1.hashCode(), productName2.hashCode());
    }

    @Test
    @DisplayName("Dado dos ProductName con valores diferentes, cuando se comparan, entonces no deben ser iguales")
    void givenTwoProductNamesWithDifferentValues_whenComparing_thenShouldNotBeEqual() {
        // Given
        ProductName productName1 = new ProductName("Smartphone X");
        ProductName productName2 = new ProductName("Laptop Pro Y");

        // Then
        assertNotEquals(productName1, productName2);
    }

    @Test
    @DisplayName("Dado un ProductName, cuando se obtiene su representación string, entonces debe incluir el valor")
    void givenProductName_whenCallingToString_thenShouldIncludeValue() {
        // Given
        String name = "Tablet Pro 12";
        ProductName productName = new ProductName(name);

        // When
        String stringRepresentation = productName.toString();

        // Then
        assertTrue(stringRepresentation.contains(name));
    }

    @ParameterizedTest
    @MethodSource("lengthBoundaryNamesProvider")
    @DisplayName("Dado nombres en los límites de longitud, cuando se crea ProductName, entonces deben manejarse correctamente")
    void givenLengthBoundaryNames_whenCreatingProductName_thenShouldHandleCorrectly(String name, boolean shouldBeValid, String expectedError) {
        if (shouldBeValid) {
            // When & Then for valid names
            assertDoesNotThrow(() -> {
                ProductName productName = new ProductName(name);
                assertEquals(name, productName.value());
            });
        } else {
            // When & Then for invalid names
            InvalidProductNameException exception = assertThrows(InvalidProductNameException.class, () ->
                    new ProductName(name)
            );
            if (expectedError != null) {
                assertTrue(exception.getMessage().contains(expectedError));
            }
        }
    }

    static Stream<Arguments> lengthBoundaryNamesProvider() {
        return Stream.of(
                Arguments.of("", false, "cannot be blank"),
                Arguments.of("A", false, "must be at least 4 characters"),     // 1 carácter
                Arguments.of("AB", false, "must be at least 4 characters"),    // 2 caracteres
                Arguments.of("ABC", false, "must be at least 4 characters"),   // 3 caracteres
                Arguments.of("ABCD", true, null),                              // 4 caracteres - mínimo
                Arguments.of("ABCDE", true, null),                             // 5 caracteres
                Arguments.of("A".repeat(60), true, null),                      // 60 caracteres - máximo
                Arguments.of("A".repeat(61), false, "cannot exceed 60 characters"), // 61 caracteres
                Arguments.of("   ", false, "cannot be blank")
                );
    }

    @Test
    @DisplayName("Dado nombres realistas de productos dentro del límite, cuando se crea ProductName, entonces deben crearse correctamente")
    void givenRealisticProductNamesWithinLimit_whenCreatingProductName_thenShouldCreateSuccessfully() {
        // Given
        String[] realisticNames = {
                "iPhone 14 Pro Max",
                "Samsung Galaxy S23",
                "MacBook Pro 16 M3",
                "Sony WH-1000XM4",
                "Nintendo Switch",
                "Canon EOS R5 Cam",
                "Dell XPS 13 Laptop",
                "PlayStation 5",
                "Microsoft Surface",
                "Bose QuietComfort"
        };

        // When & Then
        for (String name : realisticNames) {
            assertDoesNotThrow(() -> {
                ProductName productName = new ProductName(name);
                assertEquals(name, productName.value());
                assertTrue(productName.value().length() >= 4 && productName.value().length() <= 60,
                        "Name should be within limits: " + name);
            }, "Should not throw for realistic name: " + name);
        }
    }

    @Test
    @DisplayName("Dado nombres que exceden el límite, cuando se crea ProductName, entonces debe lanzar excepción")
    void givenNamesExceedingLimit_whenCreatingProductName_thenShouldThrowException() {
        // Given
        String tooLongName = "Super Ultra Mega Extreme Deluxe Premium Limited Special Edition Gaming Laptop Pro";

        // When & Then
        InvalidProductNameException exception = assertThrows(InvalidProductNameException.class, () ->
                new ProductName(tooLongName)
        );

        assertTrue(exception.getMessage().contains("cannot exceed 60 characters"));
    }

    @Test
    @DisplayName("Dado nombres con diferentes casos, cuando se crea ProductName, entonces deben preservar el caso original")
    void givenNamesWithDifferentCases_whenCreatingProductName_thenShouldPreserveOriginalCase() {
        // Given
        String lowercase = "product name test";
        String uppercase = "PRODUCT NAME TEST";
        String mixedCase = "Product Name Test";

        // When
        ProductName lower = new ProductName(lowercase);
        ProductName upper = new ProductName(uppercase);
        ProductName mixed = new ProductName(mixedCase);

        // Then
        assertEquals(lowercase, lower.value());
        assertEquals(uppercase, upper.value());
        assertEquals(mixedCase, mixed.value());
        assertNotEquals(lower, upper);
        assertNotEquals(lower, mixed);
    }

    @Test
    @DisplayName("Dado comparación con null, cuando se verifica equals, entonces debe retornar false")
    void givenNull_whenCheckingEquals_thenShouldReturnFalse() {
        // Given
        ProductName productName = new ProductName("Test Product");

        // Then
        assertNotEquals(null, productName, "Equals con null debe retornar false");
    }

    @Test
    @DisplayName("Dado nombres con caracteres especiales dentro del límite, cuando se crea ProductName, entonces deben crearse correctamente")
    void givenNamesWithSpecialCharactersWithinLimit_whenCreatingProductName_thenShouldCreateSuccessfully() {
        // Given
        String[] specialCharacterNames = {
                "Prod-Plus+",
                "Super_Prod",
                "Prod@Home",
                "Prod#123",
                "Café & Co",
                "Prod (2024)",
                "Prod [New]",
                "Prod {Special}"
        };

        // When & Then
        for (String name : specialCharacterNames) {
            assertDoesNotThrow(() -> {
                ProductName productName = new ProductName(name);
                assertEquals(name, productName.value());
                assertTrue(productName.value().length() >= 4 && productName.value().length() <= 60,
                        "Special character name should be within limits: " + name);
            });
        }
    }

    @Test
    @DisplayName("Dado nombre con exactamente 4 caracteres con espacios, cuando se crea ProductName, entonces debe crearse correctamente")
    void givenNameWithExactlyFourCharactersIncludingSpaces_whenCreatingProductName_thenShouldCreateSuccessfully() {
        // Given
        String nameWithSpaces = "A BC"; // 4 characters including space

        // When
        ProductName productName = new ProductName(nameWithSpaces);

        // Then
        assertEquals(nameWithSpaces, productName.value());
        assertEquals(4, productName.value().length());
    }
}