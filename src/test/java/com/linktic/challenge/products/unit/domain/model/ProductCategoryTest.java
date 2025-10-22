package com.linktic.challenge.products.unit.domain.model;

import com.linktic.challenge.products.domain.exception.valueobject.InvalidCategoryException;
import com.linktic.challenge.products.domain.model.ProductCategory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class ProductCategoryTest {

    private static final int MIN_LENGTH = 2;
    private static final int MAX_LENGTH = 50;

    @Test
    @DisplayName("Dado una categoría válida, cuando se crea ProductCategory, entonces debe crearse correctamente")
    void givenValidCategory_whenCreatingProductCategory_thenShouldCreateSuccessfully() {
        // Given
        String validCategory = "Electrónicos";

        // When
        ProductCategory category = new ProductCategory(validCategory);

        // Then
        assertEquals(validCategory, category.value());
    }

    @Test
    @DisplayName("Dado valor nulo, cuando se crea ProductCategory, entonces debe crearse correctamente")
    void givenNullValue_whenCreatingProductCategory_thenShouldCreateSuccessfully() {
        // When & Then
        assertDoesNotThrow(() -> {
            ProductCategory category = new ProductCategory(null);
            assertNull(category.value());
        });
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "Tecnología",
            "Hogar y Jardín",
            "Ropa-Deportiva",
            "Salud & Belleza",
            "Juguetes y Juegos",
            "Automotriz",
            "Libros, Música y Películas",
            "Alimentos y Bebidas",
            "Electrodomésticos (Grandes)",
            "Mascotas y Accesorios"
    })
    @DisplayName("Dado diferentes categorías válidas, cuando se crea ProductCategory, entonces deben crearse correctamente")
    void givenDifferentValidCategories_whenCreatingProductCategory_thenShouldCreateSuccessfully(String validCategory) {
        // When & Then
        assertDoesNotThrow(() -> {
            ProductCategory category = new ProductCategory(validCategory);
            assertEquals(validCategory, category.value());
        });
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "   ", "\t", "\n", "\r\n"})
    @DisplayName("Dado categoría vacía o en blanco, cuando se crea ProductCategory, entonces debe lanzar InvalidCategoryException")
    void givenBlankCategory_whenCreatingProductCategory_thenShouldThrowInvalidCategoryException(String blankCategory) {
        // When & Then
        InvalidCategoryException exception = assertThrows(InvalidCategoryException.class, () ->
                new ProductCategory(blankCategory)
        );

        assertEquals("Invalid category: Category cannot be blank if provided", exception.getMessage());
    }

    @Test
    @DisplayName("Dado categoría con un solo carácter, cuando se crea ProductCategory, entonces debe lanzar InvalidCategoryException")
    void givenCategoryWithSingleCharacter_whenCreatingProductCategory_thenShouldThrowInvalidCategoryException() {
        // Given
        String singleCharCategory = "A";

        // When & Then
        InvalidCategoryException exception = assertThrows(InvalidCategoryException.class, () ->
                new ProductCategory(singleCharCategory)
        );

        assertEquals("Invalid category: Category must be at least " + MIN_LENGTH + " characters long", exception.getMessage());
    }

    @Test
    @DisplayName("Dado categoría que excede longitud máxima, cuando se crea ProductCategory, entonces debe lanzar InvalidCategoryException")
    void givenCategoryExceedingMaximumLength_whenCreatingProductCategory_thenShouldThrowInvalidCategoryException() {
        // Given
        String longCategory = "Categoría extremadamente larga que excede el límite máximo permitido para el sistema";

        // When & Then
        InvalidCategoryException exception = assertThrows(InvalidCategoryException.class, () ->
                new ProductCategory(longCategory)
        );

        assertEquals("Invalid category: Category cannot exceed " + MAX_LENGTH + " characters", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "Categoría@Inválida",
            "Categoría#Especial",
            "Categoría$Premium",
            "Categoría*Estrella",
            "Categoría/División",
            "Categoría\\Inversa",
            "Categoría|Pipe",
            "Categoría<Menor",
            "Categoría>Mayor",
            "Categoría{Llave",
            "Categoría}Llave",
            "Categoría[Corchete",
            "Categoría]Corchete"
    })
    @DisplayName("Dado categoría con caracteres inválidos, cuando se crea ProductCategory, entonces debe lanzar InvalidCategoryException")
    void givenCategoryWithInvalidCharacters_whenCreatingProductCategory_thenShouldThrowInvalidCategoryException(String invalidCategory) {
        // When & Then
        InvalidCategoryException exception = assertThrows(InvalidCategoryException.class, () ->
                new ProductCategory(invalidCategory)
        );

        assertEquals("Invalid category: Category contains invalid characters", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "12345",
            "123 456",
            "12-34",
            "1234&5678"
    })
    @DisplayName("Dado categoría que solo contiene números, cuando se crea ProductCategory, entonces debe lanzar InvalidCategoryException")
    void givenCategoryWithOnlyNumbers_whenCreatingProductCategory_thenShouldThrowInvalidCategoryException(String numericCategory) {
        // When & Then
        InvalidCategoryException exception = assertThrows(InvalidCategoryException.class, () ->
                new ProductCategory(numericCategory)
        );

        assertEquals("Invalid category: Category must contain at least one letter", exception.getMessage());
    }

    @Test
    @DisplayName("Dado dos ProductCategory con el mismo valor, cuando se comparan, entonces deben ser iguales")
    void givenTwoProductCategoriesWithSameValue_whenComparing_thenShouldBeEqual() {
        // Given
        String categoryValue = "Electrónicos";
        ProductCategory category1 = new ProductCategory(categoryValue);
        ProductCategory category2 = new ProductCategory(categoryValue);

        // Then
        assertEquals(category1, category2);
        assertEquals(category1.hashCode(), category2.hashCode());
    }

    @Test
    @DisplayName("Dado dos ProductCategory con valores diferentes, cuando se comparan, entonces no deben ser iguales")
    void givenTwoProductCategoriesWithDifferentValues_whenComparing_thenShouldNotBeEqual() {
        // Given
        ProductCategory category1 = new ProductCategory("Electrónicos");
        ProductCategory category2 = new ProductCategory("Hogar");

        // Then
        assertNotEquals(category1, category2);
    }

    @Test
    @DisplayName("Dado dos ProductCategory nulas, cuando se comparan, entonces deben ser iguales")
    void givenTwoNullProductCategories_whenComparing_thenShouldBeEqual() {
        // Given
        ProductCategory category1 = new ProductCategory(null);
        ProductCategory category2 = new ProductCategory(null);

        // Then
        assertEquals(category1, category2);
        assertEquals(category1.hashCode(), category2.hashCode());
    }

    @Test
    @DisplayName("Dado un ProductCategory, cuando se obtiene su representación string, entonces debe incluir el valor")
    void givenProductCategory_whenCallingToString_thenShouldIncludeValue() {
        // Given
        String categoryValue = "Electrónicos";
        ProductCategory category = new ProductCategory(categoryValue);

        // When
        String stringRepresentation = category.toString();

        // Then
        assertTrue(stringRepresentation.contains(categoryValue));
    }

    @Test
    @DisplayName("Dado un ProductCategory nulo, cuando se obtiene su representación string, entonces debe manejarlo correctamente")
    void givenNullProductCategory_whenCallingToString_thenShouldHandleCorrectly() {
        // Given
        ProductCategory category = new ProductCategory(null);

        // When
        String stringRepresentation = category.toString();

        // Then
        assertNotNull(stringRepresentation);
    }

    @Test
    @DisplayName("Dado parámetros válidos, cuando se usa factory method of(), entonces debe crear el mismo ProductCategory")
    void givenValidParameters_whenUsingFactoryMethod_thenShouldCreateSameProductCategory() {
        // Given
        String validCategory = "Electrónicos";

        // When
        ProductCategory fromConstructor = new ProductCategory(validCategory);
        ProductCategory fromFactory = ProductCategory.of(validCategory);

        // Then
        assertEquals(fromConstructor.value(), fromFactory.value());
        assertEquals(validCategory, fromFactory.value());
    }

    @Test
    @DisplayName("Dado valor nulo, cuando se usa factory method of(), entonces debe crear ProductCategory nulo")
    void givenNullValue_whenUsingFactoryMethod_thenShouldCreateNullProductCategory() {
        // When
        ProductCategory category = ProductCategory.of(null);

        // Then
        assertNull(category.value());
    }

    @ParameterizedTest
    @MethodSource("validCategoriesProvider")
    @DisplayName("Dado diferentes categorías válidas con caracteres especiales permitidos, cuando se crea ProductCategory, entonces deben crearse correctamente")
    void givenValidCategoriesWithAllowedSpecialChars_whenCreatingProductCategory_thenShouldCreateSuccessfully(String validCategory, String description) {
        // When & Then
        assertDoesNotThrow(() -> {
            ProductCategory category = new ProductCategory(validCategory);
            assertEquals(validCategory, category.value());
        }, "Should accept valid category: " + description);
    }

    static Stream<Arguments> validCategoriesProvider() {
        return Stream.of(
                Arguments.of("AB", "Minimum length"),
                Arguments.of("Electrónicos y Tecnología", "With accents and spaces"),
                Arguments.of("Hogar & Jardín", "With ampersand"),
                Arguments.of("Ropa-Deportiva", "With hyphen"),
                Arguments.of("Libros, Música, Películas", "With commas"),
                Arguments.of("Electrodomésticos (Grandes)", "With parentheses"),
                Arguments.of("Niños 0-3 años", "With numbers and hyphen"),
                Arguments.of("Categoría con ñ y acentos áéíóú", "With all Spanish accents"),
                Arguments.of("A".repeat(MAX_LENGTH), "Maximum length")
        );
    }

    @ParameterizedTest
    @MethodSource("invalidCategoriesProvider")
    @DisplayName("Dado diferentes categorías inválidas, cuando se crea ProductCategory, entonces debe lanzar InvalidCategoryException")
    void givenInvalidCategories_whenCreatingProductCategory_thenShouldThrowInvalidCategoryException(String invalidCategory, String expectedError) {
        // When & Then
        InvalidCategoryException exception = assertThrows(InvalidCategoryException.class, () ->
                new ProductCategory(invalidCategory)
        );

        assertEquals("Invalid category: " + expectedError, exception.getMessage());
    }

    static Stream<Arguments> invalidCategoriesProvider() {
        return Stream.of(
                Arguments.of("", "Category cannot be blank if provided"),
                Arguments.of(" ", "Category cannot be blank if provided"),
                Arguments.of("A", "Category must be at least " + MIN_LENGTH + " characters long"),
                Arguments.of("Categoría@Inválida", "Category contains invalid characters"),
                Arguments.of("12345", "Category must contain at least one letter"),
                Arguments.of("12 34", "Category must contain at least one letter"),
                Arguments.of("A".repeat(MAX_LENGTH + 1), "Category cannot exceed " + MAX_LENGTH + " characters")
        );
    }

    @Test
    @DisplayName("Dado categorías realistas de e-commerce, cuando se crea ProductCategory, entonces deben crearse correctamente")
    void givenRealisticEcommerceCategories_whenCreatingProductCategory_thenShouldCreateSuccessfully() {
        // Given
        String[] realisticCategories = {
                "Smartphones y Teléfonos",
                "Laptops y Computadoras",
                "Tablets y iPads",
                "TV y Video",
                "Audio y Hi-Fi",
                "Videojuegos y Consolas",
                "Fotografía y Drones",
                "Wearables y Smartwatches",
                "Hogar Inteligente",
                "Componentes de PC"
        };

        // When & Then
        for (String category : realisticCategories) {
            assertDoesNotThrow(() -> {
                ProductCategory productCategory = new ProductCategory(category);
                assertEquals(category, productCategory.value());
                assertTrue(productCategory.value().length() >= MIN_LENGTH, "Category should meet minimum length: " + category);
                assertTrue(productCategory.value().length() <= MAX_LENGTH, "Category should not exceed maximum length: " + category);
            }, "Should accept realistic category: " + category);
        }
    }

    @Test
    @DisplayName("Dado comparación con null y otro tipo, cuando se verifica equals, entonces debe retornar false")
    void givenNullAndDifferentType_whenCheckingEquals_thenShouldReturnFalse() {
        // Given
        ProductCategory category = new ProductCategory("Electrónicos");

        // Then
        assertNotEquals(null, category, "Equals con null debe retornar false");
    }

    @Test
    @DisplayName("Dado categoría con mezcla de números y letras, cuando se crea ProductCategory, entonces debe crearse correctamente")
    void givenCategoryWithMixedNumbersAndLetters_whenCreatingProductCategory_thenShouldCreateSuccessfully() {
        // Given
        String[] mixedCategories = {
                "iPhone 14",
                "Samsung Galaxy S23",
                "Laptop Gaming RTX 4060",
                "TV 4K 55 Pulgadas",
                "Router Wi-Fi 6"
        };

        // When & Then
        for (String category : mixedCategories) {
            assertDoesNotThrow(() -> {
                ProductCategory productCategory = new ProductCategory(category);
                assertEquals(category, productCategory.value());
            }, "Should accept mixed category: " + category);
        }
    }

    @Test
    @DisplayName("Dado categorías con diferentes casos, cuando se crea ProductCategory, entonces deben preservar el caso original")
    void givenCategoriesWithDifferentCases_whenCreatingProductCategory_thenShouldPreserveOriginalCase() {
        // Given
        String lowercase = "electrónicos";
        String uppercase = "ELECTRÓNICOS";
        String mixedCase = "Electrónicos";

        // When
        ProductCategory lower = new ProductCategory(lowercase);
        ProductCategory upper = new ProductCategory(uppercase);
        ProductCategory mixed = new ProductCategory(mixedCase);

        // Then
        assertEquals(lowercase, lower.value());
        assertEquals(uppercase, upper.value());
        assertEquals(mixedCase, mixed.value());
        assertNotEquals(lower, upper);
        assertNotEquals(lower, mixed);
    }
}
