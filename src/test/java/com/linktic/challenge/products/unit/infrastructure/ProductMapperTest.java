package com.linktic.challenge.products.unit.infrastructure;

import com.linktic.challenge.products.domain.exception.valueobject.*;
import com.linktic.challenge.products.domain.model.*;
import com.linktic.challenge.products.infrastructure.persistence.entity.ProductEntity;
import com.linktic.challenge.products.infrastructure.persistence.entity.ProductSpecificationEntity;
import com.linktic.challenge.products.infrastructure.persistence.mapper.ProductMapper;
import com.linktic.challenge.products.objectmother.ProductObjectMother;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class ProductMapperTest {

    private ProductMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(ProductMapper.class);
    }

    @Test
    @DisplayName("Dado un Product dominio válido, cuando se mapea a ProductEntity, entonces debe mapear correctamente todos los campos")
    void givenValidProductDomain_whenMappedToEntity_thenAllFieldsAreMappedCorrectly() {
        // Given
        Product product = ProductObjectMother.smartphoneGalaxyXZ();

        // When
        ProductEntity entity = mapper.toEntity(product);

        // Then
        assertNotNull(entity);
        assertEquals("prod001", entity.getId());
        assertEquals("Smartphone Galaxy XZ", entity.getName());
        assertEquals("https://example.com/galaxy-xz.jpg", entity.getImageUrl());
        assertEquals("Smartphone flagship con cámara profesional", entity.getDescription());
        assertEquals(new BigDecimal("899.99"), entity.getPrice());
        assertEquals("USD", entity.getCurrency());
        assertEquals(4.7, entity.getRating());
        assertEquals("Electrónica", entity.getCategory());
        assertEquals("TechNova", entity.getBrand());
        assertNull(entity.getCreatedAt());
        assertNull(entity.getUpdatedAt());

        // Verificar especificaciones
        assertNotNull(entity.getSpecifications());
        assertEquals(5, entity.getSpecifications().size());

        // Verificar una especificación específica
        ProductSpecificationEntity screenSpec = entity.getSpecifications().stream()
                .filter(spec -> "pantalla".equals(spec.getKey()))
                .findFirst()
                .orElseThrow();
        assertEquals("6.5 pulgadas AMOLED", screenSpec.getValue());
    }

    @Test
    @DisplayName("Dado un ProductEntity válido, cuando se mapea a Product dominio, entonces debe mapear correctamente todos los campos")
    void givenValidProductEntity_whenMappedToDomain_thenAllFieldsAreMappedCorrectly() {
        // Given
        ProductEntity entity = createSmartphoneEntity();

        // When
        Product product = mapper.toDomain(entity);

        // Then
        assertNotNull(product);
        assertEquals("prod001", product.id().value());
        assertEquals("Smartphone Galaxy XZ", product.name().value());
        assertEquals("https://example.com/galaxy-xz.jpg", product.imageUrl().value());
        assertEquals("Smartphone flagship con cámara profesional", product.description().value());
        assertEquals(new BigDecimal("899.99"), product.price().value());
        assertEquals("USD", product.price().currency().getCurrencyCode());
        assertEquals(4.7, product.rating().value());
        assertEquals("Electrónica", product.category().value());
        assertEquals("TechNova", product.brand().value());

        // Verificar especificaciones
        assertNotNull(product.specifications().specs());
        assertEquals(5, product.specifications().specs().size());
        assertEquals("6.5 pulgadas AMOLED", product.specifications().specs().get("pantalla"));
        assertEquals("Snapdragon 8 Gen 2", product.specifications().specs().get("procesador"));
    }

    @Test
    @DisplayName("Dado un Product dominio con brand nulo, cuando se mapea a ProductEntity, entonces el brand en la entidad debe ser nulo")
    void givenProductDomainWithNullBrand_whenMappedToEntity_thenBrandIsNull() {
        // Given
        Product product = ProductObjectMother.smartphoneGalaxyXZ();
        Product productWithNullBrand = new Product(
                product.id(),
                product.name(),
                product.imageUrl(),
                product.description(),
                product.price(),
                product.rating(),
                product.category(),
                new ProductBrand(null),
                product.specifications()
        );

        // When
        ProductEntity entity = mapper.toEntity(productWithNullBrand);

        // Then
        assertNotNull(entity);
        assertNull(entity.getBrand());
    }

    @Test
    @DisplayName("Dado un ProductEntity con brand nulo, cuando se mapea a Product dominio, entonces el brand en el dominio debe ser nulo")
    void givenProductEntityWithNullBrand_whenMappedToDomain_thenBrandIsNull() {
        // Given
        ProductEntity entity = createSmartphoneEntity();
        entity.setBrand(null);

        // When
        Product product = mapper.toDomain(entity);

        // Then
        assertNotNull(product);
        assertNull(product.brand().value());
    }

    @Test
    @DisplayName("Dado un Product dominio con especificaciones vacías, cuando se mapea a ProductEntity, entonces las especificaciones deben estar vacías")
    void givenProductDomainWithEmptySpecifications_whenMappedToEntity_thenSpecificationsAreEmpty() {
        // Given
        Product product = ProductObjectMother.smartphoneGalaxyXZ();
        Product productWithEmptySpecs = new Product(
                product.id(),
                product.name(),
                product.imageUrl(),
                product.description(),
                product.price(),
                product.rating(),
                product.category(),
                product.brand(),
                new ProductSpecifications(Map.of())
        );

        // When
        ProductEntity entity = mapper.toEntity(productWithEmptySpecs);

        // Then
        assertNotNull(entity.getSpecifications());
        assertTrue(entity.getSpecifications().isEmpty());
    }

    @Test
    @DisplayName("Dado un ProductEntity con especificaciones vacías, cuando se mapea a Product dominio, entonces las especificaciones deben estar vacías")
    void givenProductEntityWithEmptySpecifications_whenMappedToDomain_thenSpecificationsAreEmpty() {
        // Given
        ProductEntity entity = createSmartphoneEntity();
        entity.setSpecifications(List.of());

        // When
        Product product = mapper.toDomain(entity);

        // Then
        assertNotNull(product.specifications().specs());
        assertTrue(product.specifications().specs().isEmpty());
    }

    @Test
    @DisplayName("Dado un Product dominio, cuando se actualiza un ProductEntity existente, entonces se actualizan los campos correctamente")
    void givenProductDomain_whenUpdatingEntity_thenAllFieldsAreUpdated() {
        // Given
        Product product = ProductObjectMother.smartphoneGalaxyXZ();
        ProductEntity existingEntity = new ProductEntity();
        existingEntity.setId("old-id"); // Este no debe cambiar
        existingEntity.setCreatedAt(LocalDateTime.now());
        existingEntity.setUpdatedAt(LocalDateTime.now());

        // When
        mapper.updateEntityFromDomain(product, existingEntity);

        // Then
        // El id no debe cambiar
        assertEquals("old-id", existingEntity.getId());

        // Los demás campos deben actualizarse
        assertEquals("Smartphone Galaxy XZ", existingEntity.getName());
        assertEquals("https://example.com/galaxy-xz.jpg", existingEntity.getImageUrl());
        assertEquals("Smartphone flagship con cámara profesional", existingEntity.getDescription());
        assertEquals(new BigDecimal("899.99"), existingEntity.getPrice());
        assertEquals("USD", existingEntity.getCurrency());
        assertEquals(4.7, existingEntity.getRating());
        assertEquals("Electrónica", existingEntity.getCategory());
        assertEquals("TechNova", existingEntity.getBrand());

        // Los timestamps no deben cambiar (se ignoran en el mapper)
        assertNotNull(existingEntity.getCreatedAt());
        assertNotNull(existingEntity.getUpdatedAt());
    }

    @Test
    @DisplayName("Dado diferentes productos del Object Mother, cuando se mapean de ida y vuelta, entonces deben mantener la consistencia")
    void givenDifferentProductsFromObjectMother_whenMappedBothWays_thenShouldMaintainConsistency() {
        // Given
        Product smartphone = ProductObjectMother.smartphoneGalaxyXZ();
        Product laptop = ProductObjectMother.laptopPro();

        // When
        ProductEntity smartphoneEntity = mapper.toEntity(smartphone);
        ProductEntity laptopEntity = mapper.toEntity(laptop);

        Product smartphoneFromEntity = mapper.toDomain(smartphoneEntity);
        Product laptopFromEntity = mapper.toDomain(laptopEntity);

        // Then
        assertEquals(smartphone, smartphoneFromEntity);
        assertEquals(laptop, laptopFromEntity);
        assertNotEquals(smartphoneFromEntity, laptopFromEntity);
    }

    @Test
    @DisplayName("Dado un ProductEntity con especificaciones nulas, cuando se mapea a Product dominio, entonces las especificaciones deben ser un mapa vacío")
    void givenProductEntityWithNullSpecifications_whenMappedToDomain_thenSpecificationsAreEmpty() {
        // Given
        ProductEntity entity = createSmartphoneEntity();
        entity.setSpecifications(null);

        // When
        Product product = mapper.toDomain(entity);

        // Then
        assertNotNull(product.specifications().specs());
        assertTrue(product.specifications().specs().isEmpty());
    }

    @Test
    @DisplayName("Dado un Product dominio con imagen URL por defecto, cuando se mapea a ProductEntity, entonces debe usar la URL por defecto")
    void givenProductDomainWithDefaultImageUrl_whenMappedToEntity_thenShouldUseDefaultImage() {
        // Given
        Product product = ProductObjectMother.smartphoneGalaxyXZ();
        Product productWithDefaultImage = new Product(
                product.id(),
                product.name(),
                new ProductImageUrl(null), // Esto activará la URL por defecto
                product.description(),
                product.price(),
                product.rating(),
                product.category(),
                product.brand(),
                product.specifications()
        );

        // When
        ProductEntity entity = mapper.toEntity(productWithDefaultImage);

        // Then
        assertNotNull(entity.getImageUrl());
        assertTrue(entity.getImageUrl().contains("depositphotos"));
    }

    @ParameterizedTest
    @MethodSource("productMappingProvider")
    @DisplayName("Dado diferentes productos, cuando se mapean entre dominio y entidad, entonces deben comportarse correctamente")
    void givenDifferentProducts_whenMappedBetweenDomainAndEntity_thenShouldBehaveCorrectly(
            Product product, String expectedId, String expectedName) {

        // When
        ProductEntity entity = mapper.toEntity(product);
        Product domain = mapper.toDomain(entity);

        // Then
        assertEquals(expectedId, entity.getId());
        assertEquals(expectedName, entity.getName());
        assertEquals(product, domain);
    }

    static Stream<Arguments> productMappingProvider() {
        return Stream.of(
                Arguments.of(ProductObjectMother.smartphoneGalaxyXZ(), "prod001", "Smartphone Galaxy XZ"),
                Arguments.of(ProductObjectMother.laptopPro(), "prod002", "Laptop Pro Ultra")
        );
    }

    @Test
    @DisplayName("Dado un ProductEntity con timestamps, cuando se mapea a Product dominio, entonces los timestamps deben ignorarse")
    void givenProductEntityWithTimestamps_whenMappedToDomain_thenTimestampsAreIgnored() {
        // Given
        ProductEntity entity = createSmartphoneEntity();
        LocalDateTime now = LocalDateTime.now();
        entity.setCreatedAt(now);
        entity.setUpdatedAt(now);

        // When
        Product product = mapper.toDomain(entity);

        // Then
        assertNotNull(product);
        // El dominio no tiene timestamps, así que no hay nada que verificar excepto que no falle
        assertEquals("prod001", product.id().value());
    }

    @Test
    @DisplayName("Dado un Product dominio con diferentes monedas, cuando se mapea a ProductEntity, entonces la moneda debe serializarse correctamente")
    void givenProductDomainWithDifferentCurrency_whenMappedToEntity_thenCurrencyIsSerializedCorrectly() {
        // Given
        Product baseProduct = ProductObjectMother.smartphoneGalaxyXZ();
        Product productWithEUR = new Product(
                baseProduct.id(),
                baseProduct.name(),
                baseProduct.imageUrl(),
                baseProduct.description(),
                new ProductPrice(new BigDecimal("799.99"), java.util.Currency.getInstance("EUR")),
                baseProduct.rating(),
                baseProduct.category(),
                baseProduct.brand(),
                baseProduct.specifications()
        );

        // When
        ProductEntity entity = mapper.toEntity(productWithEUR);

        // Then
        assertEquals("EUR", entity.getCurrency());
        assertEquals(new BigDecimal("799.99"), entity.getPrice());
    }

    @Test
    @DisplayName("Dado un ProductEntity con moneda diferente, cuando se mapea a Product dominio, entonces la moneda debe deserializarse correctamente")
    void givenProductEntityWithDifferentCurrency_whenMappedToDomain_thenCurrencyIsDeserializedCorrectly() {
        // Given
        ProductEntity entity = createSmartphoneEntity();
        entity.setCurrency("EUR");
        entity.setPrice(new BigDecimal("799.99"));

        // When
        Product product = mapper.toDomain(entity);

        // Then
        assertEquals("EUR", product.price().currency().getCurrencyCode());
        assertEquals(new BigDecimal("799.99"), product.price().value());
    }

    @Test
    @DisplayName("Dado mapeo de especificaciones individual, cuando se convierte de mapa a lista, entonces debe mantener los valores")
    void givenSpecificationsMap_whenConvertingToList_thenShouldMaintainValues() {
        // Given
        Map<String, String> specs = Map.of(
                "key1", "value1",
                "key2", "value2"
        );

        // When
        List<ProductSpecificationEntity> result = mapper.mapToSpecificationEntities(specs);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(spec -> "key1".equals(spec.getKey()) && "value1".equals(spec.getValue())));
        assertTrue(result.stream().anyMatch(spec -> "key2".equals(spec.getKey()) && "value2".equals(spec.getValue())));
    }

    @Test
    @DisplayName("Dado mapeo de especificaciones nulas, cuando se convierte, entonces debe retornar lista vacía")
    void givenNullSpecifications_whenConverting_thenShouldReturnEmptyList() {
        // When
        List<ProductSpecificationEntity> result = mapper.mapToSpecificationEntities(null);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Dado mapeo de lista de especificaciones a mapa, cuando se convierte, entonces debe mantener los valores")
    void givenSpecificationsList_whenConvertingToMap_thenShouldMaintainValues() {
        // Given
        List<ProductSpecificationEntity> specs = List.of(
                ProductSpecificationEntity.builder().key("key1").value("value1").build(),
                ProductSpecificationEntity.builder().key("key2").value("value2").build()
        );

        // When
        Map<String, String> result = mapper.specificationsToMap(specs);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("value1", result.get("key1"));
        assertEquals("value2", result.get("key2"));
    }

    // Método helper para crear una ProductEntity de ejemplo
    private ProductEntity createSmartphoneEntity() {
        return ProductEntity.builder()
                .id("prod001")
                .name("Smartphone Galaxy XZ")
                .imageUrl("https://example.com/galaxy-xz.jpg")
                .description("Smartphone flagship con cámara profesional")
                .price(new BigDecimal("899.99"))
                .currency("USD")
                .rating(4.7)
                .category("Electrónica")
                .brand("TechNova")
                .specifications(List.of(
                        ProductSpecificationEntity.builder()
                                .key("pantalla")
                                .value("6.5 pulgadas AMOLED")
                                .build(),
                        ProductSpecificationEntity.builder()
                                .key("procesador")
                                .value("Snapdragon 8 Gen 2")
                                .build(),
                        ProductSpecificationEntity.builder()
                                .key("memoria")
                                .value("256GB")
                                .build(),
                        ProductSpecificationEntity.builder()
                                .key("camara")
                                .value("108MP + 12MP + 5MP")
                                .build(),
                        ProductSpecificationEntity.builder()
                                .key("bateria")
                                .value("4800mAh")
                                .build()
                ))
                .build();
    }

    @Test
    @DisplayName("Dado un ProductEntity con category nula, cuando se mapea a Product dominio, entonces la category en el dominio debe ser nula")
    void givenProductEntityWithNullCategory_whenMappedToDomain_thenCategoryIsNull() {
        // Given
        ProductEntity entity = createSmartphoneEntity();
        entity.setCategory(null);

        // When
        Product product = mapper.toDomain(entity);

        // Then
        assertNotNull(product);
        assertNotNull(product.category()); // El objeto ProductCategory debe existir
        assertNull(product.category().value()); // Pero su valor interno debe ser null
    }

    @Test
    @DisplayName("Dado un Product dominio con category nula, cuando se mapea a ProductEntity, entonces el category en la entidad debe ser nulo")
    void givenProductDomainWithNullCategory_whenMappedToEntity_thenCategoryIsNull() {
        // Given
        Product product = ProductObjectMother.smartphoneGalaxyXZ();
        Product productWithNullCategory = new Product(
                product.id(),
                product.name(),
                product.imageUrl(),
                product.description(),
                product.price(),
                product.rating(),
                new ProductCategory(null),
                product.brand(),
                product.specifications()
        );

        // When
        ProductEntity entity = mapper.toEntity(productWithNullCategory);

        // Then
        assertNotNull(entity);
        assertNull(entity.getCategory());
    }

    @Test
    @DisplayName("Dado specifications nulas en specsToEntities, cuando se convierte, entonces debe retornar lista vacía")
    void givenNullSpecificationsInSpecsToEntities_whenConverting_thenShouldReturnEmptyList() {
        // When
        List<ProductSpecificationEntity> result = mapper.specsToEntities(null);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Dado specifications con specs nulas en specsToEntities, cuando se convierte, entonces debe retornar lista vacía")
    void givenSpecificationsWithNullSpecsInSpecsToEntities_whenConverting_thenShouldReturnEmptyList() {
        // Given
        ProductSpecifications specsWithNullMap = new ProductSpecifications(null);

        // When
        List<ProductSpecificationEntity> result = mapper.specsToEntities(specsWithNullMap);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Dado specifications con specs vacías en specsToEntities, cuando se convierte, entonces debe retornar lista vacía")
    void givenSpecificationsWithEmptySpecsInSpecsToEntities_whenConverting_thenShouldReturnEmptyList() {
        // Given
        ProductSpecifications specsWithEmptyMap = new ProductSpecifications(Map.of());

        // When
        List<ProductSpecificationEntity> result = mapper.specsToEntities(specsWithEmptyMap);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Dado specificationsToMap con lista nula, cuando se convierte, entonces debe retornar mapa vacío")
    void givenNullListInSpecificationsToMap_whenConverting_thenShouldReturnEmptyMap() {
        // When
        Map<String, String> result = mapper.specificationsToMap(null);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Dado mapToSpecificationEntities con mapa vacío, cuando se convierte, entonces debe retornar lista vacía")
    void givenEmptyMapInMapToSpecificationEntities_whenConverting_thenShouldReturnEmptyList() {
        // Given
        Map<String, String> emptyMap = Map.of();

        // When
        List<ProductSpecificationEntity> result = mapper.mapToSpecificationEntities(emptyMap);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Dado mapSpecificationsFromEntities con lista nula, cuando se convierte, entonces debe retornar ProductSpecifications vacío")
    void givenNullListInMapSpecificationsFromEntities_whenConverting_thenShouldReturnEmptyProductSpecifications() {
        // When
        ProductSpecifications result = mapper.mapSpecificationsFromEntities(null);

        // Then
        assertNotNull(result);
        assertNotNull(result.specs());
        assertTrue(result.specs().isEmpty());
    }

    @Test
    @DisplayName("Dado mapSpecificationsFromEntities con lista vacía, cuando se convierte, entonces debe retornar ProductSpecifications vacío")
    void givenEmptyListInMapSpecificationsFromEntities_whenConverting_thenShouldReturnEmptyProductSpecifications() {
        // Given
        List<ProductSpecificationEntity> emptyList = List.of();

        // When
        ProductSpecifications result = mapper.mapSpecificationsFromEntities(emptyList);

        // Then
        assertNotNull(result);
        assertNotNull(result.specs());
        assertTrue(result.specs().isEmpty());
    }

    @Test
    @DisplayName("Dado updateEntityFromDomain con product nulo, cuando se actualiza, entonces no debe lanzar excepción")
    void givenNullProductInUpdateEntityFromDomain_whenUpdating_thenShouldNotThrowException() {
        // Given
        ProductEntity entity = createSmartphoneEntity();

        // When & Then
        assertDoesNotThrow(() -> mapper.updateEntityFromDomain(null, entity));
    }

    @Test
    @DisplayName("Dado updateEntityFromDomain con entity existente, no debe lanzar excepción")
    void givenEntityInstance_whenUpdating_thenNoException() {
        // Given
        Product product = ProductObjectMother.smartphoneGalaxyXZ();
        ProductEntity entity = new ProductEntity(); // o usar tu builder

        // When & Then
        assertDoesNotThrow(() -> mapper.updateEntityFromDomain(product, entity));
    }


    @Test
    @DisplayName("Dado toEntity con product nulo, cuando se mapea, entonces debe retornar null")
    void givenNullProductInToEntity_whenMapping_thenShouldReturnNull() {
        // When
        ProductEntity result = mapper.toEntity(null);

        // Then
        assertNull(result);
    }

    @Test
    @DisplayName("Dado toDomain con entity nula, cuando se mapea, entonces debe retornar null")
    void givenNullEntityInToDomain_whenMapping_thenShouldReturnNull() {
        // When
        Product result = mapper.toDomain(null);

        // Then
        assertNull(result);
    }

    @Test
    @DisplayName("Dado mapImageUrl con string vacío, cuando se crea ProductImageUrl, entonces debe usar imagen por defecto")
    void givenEmptyStringInMapImageUrl_whenCreatingProductImageUrl_thenShouldUseDefaultImage() {
        // When
        ProductImageUrl result = mapper.mapImageUrl("");

        // Then
        assertNotNull(result);
        assertNotNull(result.value());
        assertTrue(result.value().contains("depositphotos"));
    }

    @Test
    @DisplayName("Dado mapImageUrl con string de espacios en blanco, cuando se crea ProductImageUrl, entonces debe usar imagen por defecto")
    void givenBlankStringInMapImageUrl_whenCreatingProductImageUrl_thenShouldUseDefaultImage() {
        // When
        ProductImageUrl result = mapper.mapImageUrl("   ");

        // Then
        assertNotNull(result);
        assertNotNull(result.value());
        assertTrue(result.value().contains("depositphotos"));
    }

    @Test
    @DisplayName("Dado mapPrice con currency code inválido, cuando se crea ProductPrice, entonces debe lanzar excepción")
    void givenInvalidCurrencyCodeInMapPrice_whenCreatingProductPrice_thenShouldThrowException() {
        // Given
        BigDecimal price = new BigDecimal("100.00");
        String invalidCurrencyCode = "INVALID";

        // When & Then
        assertThrows(IllegalArgumentException.class,
                () -> mapper.mapPrice(price, invalidCurrencyCode));
    }

    @Test
    @DisplayName("Dado mapPrice con currency code nulo, cuando se crea ProductPrice, entonces debe lanzar excepción")
    void givenNullCurrencyCodeInMapPrice_whenCreatingProductPrice_thenShouldThrowException() {
        // Given
        BigDecimal price = new BigDecimal("100.00");

        // When & Then
        assertThrows(NullPointerException.class,
                () -> mapper.mapPrice(price, null));
    }

    @Test
    @DisplayName("Dado mapRating con valor fuera de rango, cuando se crea ProductRating, entonces debe lanzar excepción")
    void givenOutOfRangeRatingInMapRating_whenCreatingProductRating_thenShouldThrowException() {
        // Given
        Double outOfRangeRating = 5.1;

        // When & Then
        assertThrows(InvalidRatingException.class,
                () -> mapper.mapRating(outOfRangeRating));
    }

    @Test
    @DisplayName("Dado mapRating con valor negativo, cuando se crea ProductRating, entonces debe lanzar excepción")
    void givenNegativeRatingInMapRating_whenCreatingProductRating_thenShouldThrowException() {
        // Given
        Double negativeRating = -1.0;

        // When & Then
        assertThrows(InvalidRatingException.class,
                () -> mapper.mapRating(negativeRating));
    }

    @Test
    @DisplayName("Dado mapRating con NaN, cuando se crea ProductRating, entonces debe lanzar excepción")
    void givenNaNRatingInMapRating_whenCreatingProductRating_thenShouldThrowException() {
        // Given
        Double nanRating = Double.NaN;

        // When & Then
        assertThrows(InvalidRatingException.class,
                () -> mapper.mapRating(nanRating));
    }

    @Test
    @DisplayName("Dado mapRating con infinito positivo, cuando se crea ProductRating, entonces debe lanzar excepción")
    void givenPositiveInfinityRatingInMapRating_whenCreatingProductRating_thenShouldThrowException() {
        // Given
        Double infinityRating = Double.POSITIVE_INFINITY;

        // When & Then
        assertThrows(InvalidRatingException.class,
                () -> mapper.mapRating(infinityRating));
    }

    @Test
    @DisplayName("Dado mapRating con infinito negativo, cuando se crea ProductRating, entonces debe lanzar excepción")
    void givenNegativeInfinityRatingInMapRating_whenCreatingProductRating_thenShouldThrowException() {
        // Given
        Double negativeInfinityRating = Double.NEGATIVE_INFINITY;

        // When & Then
        assertThrows(InvalidRatingException.class,
                () -> mapper.mapRating(negativeInfinityRating));
    }

    @Test
    @DisplayName("Dado mapBrand con string vacío, cuando se crea ProductBrand, entonces debe lanzar excepción")
    void givenEmptyStringInMapBrand_whenCreatingProductBrand_thenShouldThrowException() {
        // When & Then
        assertThrows(InvalidBrandException.class,
                () -> mapper.mapBrand(""));
    }

    @Test
    @DisplayName("Dado mapBrand con string de espacios en blanco, cuando se crea ProductBrand, entonces debe lanzar excepción")
    void givenBlankStringInMapBrand_whenCreatingProductBrand_thenShouldThrowException() {
        // When & Then
        assertThrows(InvalidBrandException.class,
                () -> mapper.mapBrand("   "));
    }

    @Test
    @DisplayName("Dado mapCategory con string vacío, cuando se crea ProductCategory, entonces debe lanzar excepción")
    void givenEmptyStringInMapCategory_whenCreatingProductCategory_thenShouldThrowException() {
        // When & Then
        assertThrows(InvalidCategoryException.class,
                () -> mapper.mapCategory(""));
    }

    @Test
    @DisplayName("Dado mapCategory con string de espacios en blanco, cuando se crea ProductCategory, entonces debe lanzar excepción")
    void givenBlankStringInMapCategory_whenCreatingProductCategory_thenShouldThrowException() {
        // When & Then
        assertThrows(InvalidCategoryException.class,
                () -> mapper.mapCategory("   "));
    }

    @Test
    @DisplayName("Dado mapDescription con string vacío, cuando se crea ProductDescription, entonces debe lanzar excepción")
    void givenEmptyStringInMapDescription_whenCreatingProductDescription_thenShouldThrowException() {
        // When & Then
        assertThrows(InvalidProductDescriptionException.class,
                () -> mapper.mapDescription(""));
    }

    @Test
    @DisplayName("Dado mapDescription con string de espacios en blanco, cuando se crea ProductDescription, entonces debe lanzar excepción")
    void givenBlankStringInMapDescription_whenCreatingProductDescription_thenShouldThrowException() {
        // When & Then
        assertThrows(InvalidProductDescriptionException.class,
                () -> mapper.mapDescription("   "));
    }

    @Test
    @DisplayName("Dado mapName con string vacío, cuando se crea ProductName, entonces debe lanzar excepción")
    void givenEmptyStringInMapName_whenCreatingProductName_thenShouldThrowException() {
        // When & Then
        assertThrows(InvalidProductNameException.class,
                () -> mapper.mapName(""));
    }

    @Test
    @DisplayName("Dado mapName con string de espacios en blanco, cuando se crea ProductName, entonces debe lanzar excepción")
    void givenBlankStringInMapName_whenCreatingProductName_thenShouldThrowException() {
        // When & Then
        assertThrows(InvalidProductNameException.class,
                () -> mapper.mapName("   "));
    }

    @Test
    @DisplayName("Dado mapId con string vacío, cuando se crea ProductId, entonces debe lanzar excepción")
    void givenEmptyStringInMapId_whenCreatingProductId_thenShouldThrowException() {
        // When & Then
        assertThrows(InvalidProductIdException.class,
                () -> mapper.mapId(""));
    }

    @Test
    @DisplayName("Dado mapId con string de espacios en blanco, cuando se crea ProductId, entonces debe lanzar excepción")
    void givenBlankStringInMapId_whenCreatingProductId_thenShouldThrowException() {
        // When & Then
        assertThrows(InvalidProductIdException.class,
                () -> mapper.mapId("   "));
    }

    @Test
    @DisplayName("Dado mapId con caracteres inválidos, cuando se crea ProductId, entonces debe lanzar excepción")
    void givenInvalidCharactersInMapId_whenCreatingProductId_thenShouldThrowException() {
        // When & Then
        assertThrows(InvalidProductIdException.class,
                () -> mapper.mapId("invalid@id"));
    }
}