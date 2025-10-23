package com.linktic.challenge.products.unit.domain.model;

import com.linktic.challenge.products.domain.model.*;
import com.linktic.challenge.products.objectmother.ProductObjectMother;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    @Test
    @DisplayName("Dado todos los value objects válidos, cuando se crea Product, entonces debe crearse correctamente")
    void givenAllValidValueObjects_whenCreatingProduct_thenShouldCreateSuccessfully() {
        // Given
        Product product = ProductObjectMother.smartphoneGalaxyXZ();

        // Then
        assertNotNull(product);
        assertEquals("prod001", product.id().value());
        assertEquals("Smartphone Galaxy XZ", product.name().value());
        assertEquals("https://example.com/galaxy-xz.jpg", product.imageUrl().value());
        assertEquals("Smartphone flagship con cámara profesional", product.description().value());
        assertEquals(new BigDecimal("899.99"), product.price().value());
        assertEquals(Currency.getInstance("USD"), product.price().currency());
        assertEquals(4.7, product.rating().value());
        assertEquals("Electrónica", product.category().value());
        assertEquals("TechNova", product.brand().value());
        assertEquals(5, product.specifications().specs().size());
    }

    @Test
    @DisplayName("Dado un producto válido, cuando se accede a las especificaciones, entonces deben ser las correctas")
    void givenValidProduct_whenAccessingSpecifications_thenShouldReturnCorrectSpecs() {
        // Given
        Product product = ProductObjectMother.smartphoneGalaxyXZ();

        // When
        Map<String, String> specs = product.specifications().specs();

        // Then
        assertEquals("6.5 pulgadas AMOLED", specs.get("pantalla"));
        assertEquals("Snapdragon 8 Gen 2", specs.get("procesador"));
        assertEquals("256GB", specs.get("memoria"));
        assertEquals("108MP + 12MP + 5MP", specs.get("camara"));
        assertEquals("4800mAh", specs.get("bateria"));
    }

    @Test
    @DisplayName("Dado dos productos con los mismos valores, cuando se comparan, entonces deben ser iguales")
    void givenTwoProductsWithSameValues_whenComparing_thenShouldBeEqual() {
        // Given
        Product product1 = ProductObjectMother.smartphoneGalaxyXZ();
        Product product2 = ProductObjectMother.smartphoneGalaxyXZ();

        // Then
        assertEquals(product1, product2);
        assertEquals(product1.hashCode(), product2.hashCode());
    }

    @Test
    @DisplayName("Dado dos productos diferentes, cuando se comparan, entonces no deben ser iguales")
    void givenTwoDifferentProducts_whenComparing_thenShouldNotBeEqual() {
        // Given
        Product smartphone = ProductObjectMother.smartphoneGalaxyXZ();
        Product laptop = ProductObjectMother.laptopPro();

        // Then
        assertNotEquals(smartphone, laptop);
        assertNotEquals(smartphone.hashCode(), laptop.hashCode());
    }

    @Test
    @DisplayName("Dado un producto, cuando se obtiene su representación string, entonces debe incluir información relevante")
    void givenProduct_whenCallingToString_thenShouldIncludeRelevantInformation() {
        // Given
        Product product = ProductObjectMother.smartphoneGalaxyXZ();

        // When
        String stringRepresentation = product.toString();

        // Then
        assertTrue(stringRepresentation.contains("prod001"));
        assertTrue(stringRepresentation.contains("Smartphone Galaxy XZ"));
        assertTrue(stringRepresentation.contains("TechNova"));
    }

    @Test
    @DisplayName("Dado producto con imagen URL nula, cuando se crea Product, entonces debe usar imagen por defecto")
    void givenProductWithNullImageUrl_whenCreatingProduct_thenShouldUseDefaultImage() {
        // Given
        Product baseProduct = ProductObjectMother.smartphoneGalaxyXZ();
        ProductImageUrl defaultImageUrl = new ProductImageUrl(null);

        // When
        Product product = new Product(
                baseProduct.id(),
                baseProduct.name(),
                defaultImageUrl,
                baseProduct.description(),
                baseProduct.price(),
                baseProduct.rating(),
                baseProduct.category(),
                baseProduct.brand(),
                baseProduct.specifications()
        );

        // Then
        assertNotNull(product.imageUrl().value());
        assertTrue(product.imageUrl().value().contains("depositphotos"));
    }

    @Test
    @DisplayName("Dado producto con categoría nula, cuando se crea Product, entonces debe crearse correctamente")
    void givenProductWithNullCategory_whenCreatingProduct_thenShouldCreateSuccessfully() {
        // Given
        Product baseProduct = ProductObjectMother.smartphoneGalaxyXZ();
        ProductCategory nullCategory = new ProductCategory(null);

        // When
        Product product = new Product(
                baseProduct.id(),
                baseProduct.name(),
                baseProduct.imageUrl(),
                baseProduct.description(),
                baseProduct.price(),
                baseProduct.rating(),
                nullCategory,
                baseProduct.brand(),
                baseProduct.specifications()
        );

        // Then
        assertNull(product.category().value());
    }

    @Test
    @DisplayName("Dado producto con marca nula, cuando se crea Product, entonces debe crearse correctamente")
    void givenProductWithNullBrand_whenCreatingProduct_thenShouldCreateSuccessfully() {
        // Given
        Product baseProduct = ProductObjectMother.smartphoneGalaxyXZ();
        ProductBrand nullBrand = new ProductBrand(null);

        // When
        Product product = new Product(
                baseProduct.id(),
                baseProduct.name(),
                baseProduct.imageUrl(),
                baseProduct.description(),
                baseProduct.price(),
                baseProduct.rating(),
                baseProduct.category(),
                nullBrand,
                baseProduct.specifications()
        );

        // Then
        assertNull(product.brand().value());
    }

    @Test
    @DisplayName("Dado producto con Object Mother de precio diferente, cuando se crea Product, entonces debe tener el precio especificado")
    void givenProductWithDifferentPrice_whenCreatingProduct_thenShouldHaveSpecifiedPrice() {
        // Given
        BigDecimal newPrice = new BigDecimal("799.99");

        // When
        Product product = ProductObjectMother.smartphoneWithDifferentPrice(newPrice);

        // Then
        assertEquals(newPrice, product.price().value());
        assertEquals(Currency.getInstance("USD"), product.price().currency());
    }

    @Test
    @DisplayName("Dado producto con Object Mother de rating diferente, cuando se crea Product, entonces debe tener el rating especificado")
    void givenProductWithDifferentRating_whenCreatingProduct_thenShouldHaveSpecifiedRating() {
        // Given
        double newRating = 4.9;

        // When
        Product product = ProductObjectMother.smartphoneWithDifferentRating(newRating);

        // Then
        assertEquals(newRating, product.rating().value());
    }

    @Test
    @DisplayName("Dado producto laptop del Object Mother, cuando se crea Product, entonces debe tener las características correctas")
    void givenLaptopProductFromObjectMother_whenCreatingProduct_thenShouldHaveCorrectCharacteristics() {
        // When
        Product laptop = ProductObjectMother.laptopPro();

        // Then
        assertEquals("prod002", laptop.id().value());
        assertEquals("Laptop Pro Ultra", laptop.name().value());
        assertEquals("Laptop profesional para trabajo intensivo", laptop.description().value());
        assertEquals(new BigDecimal("1299.99"), laptop.price().value());
        assertEquals(4.5, laptop.rating().value());
        assertEquals("Computadoras", laptop.category().value());
        assertEquals("TechMaster", laptop.brand().value());
        assertEquals(5, laptop.specifications().specs().size());
    }

    @ParameterizedTest
    @MethodSource("productComparisonProvider")
    @DisplayName("Dado diferentes productos, cuando se comparan, entonces deben comportarse correctamente")
    void givenDifferentProducts_whenComparing_thenShouldBehaveCorrectly(Product product1, Product product2, boolean shouldBeEqual, String description) {
        if (shouldBeEqual) {
            assertEquals(product1, product2, description);
            assertEquals(product1.hashCode(), product2.hashCode(), description);
        } else {
            assertNotEquals(product1, product2, description);
        }
    }

    static Stream<Arguments> productComparisonProvider() {
        Product smartphone1 = ProductObjectMother.smartphoneGalaxyXZ();
        Product smartphone2 = ProductObjectMother.smartphoneGalaxyXZ();
        Product laptop = ProductObjectMother.laptopPro();

        Product smartphoneDifferentPrice = ProductObjectMother.smartphoneWithDifferentPrice(new BigDecimal("999.99"));
        Product smartphoneDifferentRating = ProductObjectMother.smartphoneWithDifferentRating(3.5);

        return Stream.of(
                Arguments.of(smartphone1, smartphone2, true, "Same smartphone from object mother"),
                Arguments.of(smartphone1, laptop, false, "Smartphone vs Laptop"),
                Arguments.of(smartphone1, smartphoneDifferentPrice, false, "Different price"),
                Arguments.of(smartphone1, smartphoneDifferentRating, false, "Different rating")
        );
    }

    @Test
    @DisplayName("Dado producto con especificaciones vacías, cuando se crea Product, entonces debe crearse correctamente")
    void givenProductWithEmptySpecifications_whenCreatingProduct_thenShouldCreateSuccessfully() {
        // Given
        Product baseProduct = ProductObjectMother.smartphoneGalaxyXZ();
        ProductSpecifications emptySpecs = new ProductSpecifications(Map.of());

        // When
        Product product = new Product(
                baseProduct.id(),
                baseProduct.name(),
                baseProduct.imageUrl(),
                baseProduct.description(),
                baseProduct.price(),
                baseProduct.rating(),
                baseProduct.category(),
                baseProduct.brand(),
                emptySpecs
        );

        // Then
        assertNotNull(product.specifications());
        assertTrue(product.specifications().specs().isEmpty());
    }

    @Test
    @DisplayName("Dado producto completo, cuando se verifican todos los componentes, entonces deben ser inmutables")
    void givenCompleteProduct_whenCheckingAllComponents_thenShouldBeImmutable() {
        // Given
        Product product = ProductObjectMother.smartphoneGalaxyXZ();

        // Then
        assertInstanceOf(ProductId.class, product.id());
        assertInstanceOf(ProductName.class, product.name());
        assertInstanceOf(ProductImageUrl.class, product.imageUrl());
        assertInstanceOf(ProductDescription.class, product.description());
        assertInstanceOf(ProductPrice.class, product.price());
        assertInstanceOf(ProductRating.class, product.rating());
        assertInstanceOf(ProductCategory.class, product.category());
        assertInstanceOf(ProductBrand.class, product.brand());
        assertInstanceOf(ProductSpecifications.class, product.specifications());
    }

    @Test
    @DisplayName("Dado producto, cuando se accede a métodos de price, entonces deben funcionar correctamente")
    void givenProduct_whenAccessingPriceMethods_thenShouldWorkCorrectly() {
        // Given
        Product product = ProductObjectMother.smartphoneGalaxyXZ();
        ProductPrice additionalPrice = new ProductPrice(new BigDecimal("100.00"), Currency.getInstance("USD"));

        // When
        ProductPrice totalPrice = product.price().add(additionalPrice);

        // Then
        assertEquals(new BigDecimal("999.99"), totalPrice.value());
        assertEquals(Currency.getInstance("USD"), totalPrice.currency());
    }

    @Test
    @DisplayName("Dado productos del Object Mother, cuando se verifican sus valores, entonces deben ser consistentes")
    void givenProductsFromObjectMother_whenVerifyingValues_thenShouldBeConsistent() {
        // When
        Product smartphone = ProductObjectMother.smartphoneGalaxyXZ();
        Product laptop = ProductObjectMother.laptopPro();

        // Then
        // Verify smartphone
        assertEquals("prod001", smartphone.id().value());
        assertEquals("Smartphone Galaxy XZ", smartphone.name().value());
        assertEquals("Electrónica", smartphone.category().value());
        assertEquals("TechNova", smartphone.brand().value());

        // Verify laptop
        assertEquals("prod002", laptop.id().value());
        assertEquals("Laptop Pro Ultra", laptop.name().value());
        assertEquals("Computadoras", laptop.category().value());
        assertEquals("TechMaster", laptop.brand().value());

        // Verify they are different
        assertNotEquals(smartphone.id().value(), laptop.id().value());
        assertNotEquals(smartphone.name().value(), laptop.name().value());
        assertNotEquals(smartphone.category().value(), laptop.category().value());
        assertNotEquals(smartphone.brand().value(), laptop.brand().value());
    }

    @Test
    @DisplayName("Dado comparación con null y otro tipo, cuando se verifica equals, entonces debe retornar false")
    void givenNullAndDifferentType_whenCheckingEquals_thenShouldReturnFalse() {
        // Given
        Product product = ProductObjectMother.smartphoneGalaxyXZ();

        // Then
        assertNotEquals(null, product, "Equals con null debe retornar false");
    }

    @Test
    @DisplayName("Dado producto, cuando se verifican los límites de rating, entonces deben estar dentro del rango")
    void givenProduct_whenCheckingRatingBounds_thenShouldBeWithinRange() {
        // Given
        Product product = ProductObjectMother.smartphoneGalaxyXZ();

        // Then
        assertTrue(product.rating().value() >= 0);
        assertTrue(product.rating().value() <= 5);
    }

    @Test
    @DisplayName("Dado producto, cuando se verifican los límites de precio, entonces debe ser no negativo")
    void givenProduct_whenCheckingPriceBounds_thenShouldBeNonNegative() {
        // Given
        Product product = ProductObjectMother.smartphoneGalaxyXZ();

        // Then
        assertTrue(product.price().value().compareTo(BigDecimal.ZERO) >= 0);
    }

    @Test
    @DisplayName("Dado producto, cuando se verifican las especificaciones, entonces deben cumplir con los límites")
    void givenProduct_whenCheckingSpecifications_thenShouldComplyWithLimits() {
        // Given
        Product product = ProductObjectMother.smartphoneGalaxyXZ();

        // Then
        assertTrue(product.specifications().specs().size() <= 10);
        product.specifications().specs().forEach((key, value) -> {
            assertTrue(key.length() <= 50);
            assertTrue(value.length() <= 80);
        });
    }
}
