package com.linktic.challenge.products.unit.application.mapper;

import com.linktic.challenge.products.application.dto.CreateProductDto;
import com.linktic.challenge.products.application.dto.ProductDto;
import com.linktic.challenge.products.application.dto.UpdateProductDto;
import com.linktic.challenge.products.application.mapper.ProductMapper;
import com.linktic.challenge.products.domain.exception.mapper.ProductMapperException;
import com.linktic.challenge.products.domain.model.*;
import com.linktic.challenge.products.objectmother.ProductObjectMother;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ProductMapperTest {

    private ProductMapper productMapper;

    @BeforeEach
    void setUp() {
        productMapper = new ProductMapper();
    }

    @Test
    @DisplayName("Dado un Product dominio válido, cuando se mapea a ProductDto, entonces debe mapear correctamente todos los campos")
    void givenValidProductDomain_whenMappedToDto_thenAllFieldsAreMappedCorrectly() {
        // Given
        Product product = ProductObjectMother.smartphoneGalaxyXZ();

        // When
        ProductDto dto = productMapper.toDto(product);

        // Then
        assertNotNull(dto);
        assertEquals("prod001", dto.id());
        assertEquals("Smartphone Galaxy XZ", dto.name());
        assertEquals("https://example.com/galaxy-xz.jpg", dto.imageUrl());
        assertEquals("Smartphone flagship con cámara profesional", dto.description());
        assertEquals(new BigDecimal("899.99"), dto.price());
        assertEquals("USD", dto.currency());
        assertEquals(4.7, dto.rating());
        assertEquals("Electrónica", dto.category());
        assertEquals("TechNova", dto.brand());

        // Verificar especificaciones
        assertNotNull(dto.specifications());
        assertEquals(5, dto.specifications().size());
        assertEquals("6.5 pulgadas AMOLED", dto.specifications().get("pantalla"));
        assertEquals("Snapdragon 8 Gen 2", dto.specifications().get("procesador"));
    }

    @Test
    @DisplayName("Dado un Product dominio con rating nulo, cuando se mapea a ProductDto, entonces el rating debe ser nulo")
    void givenProductDomainWithNullRating_whenMappedToDto_thenRatingIsNull() {
        // Given
        Product product = ProductObjectMother.smartphoneGalaxyXZ();
        Product productWithNullRating = new Product(
                product.id(),
                product.name(),
                product.imageUrl(),
                product.description(),
                product.price(),
                null, // Rating nulo
                product.category(),
                product.brand(),
                product.specifications()
        );

        // When
        ProductDto dto = productMapper.toDto(productWithNullRating);

        // Then
        assertNotNull(dto);
        assertNull(dto.rating());
    }

    @Test
    @DisplayName("Dado un Product dominio con brand nulo, cuando se mapea a ProductDto, entonces el brand debe ser nulo")
    void givenProductDomainWithNullBrand_whenMappedToDto_thenBrandIsNull() {
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
                new ProductBrand(null), // Brand nulo
                product.specifications()
        );

        // When
        ProductDto dto = productMapper.toDto(productWithNullBrand);

        // Then
        assertNotNull(dto);
        assertNull(dto.brand());
    }

    @Test
    @DisplayName("Dado un Product dominio con category nula, cuando se mapea a ProductDto, entonces la category debe ser nula")
    void givenProductDomainWithNullCategory_whenMappedToDto_thenCategoryIsNull() {
        // Given
        Product product = ProductObjectMother.smartphoneGalaxyXZ();
        Product productWithNullCategory = new Product(
                product.id(),
                product.name(),
                product.imageUrl(),
                product.description(),
                product.price(),
                product.rating(),
                new ProductCategory(null), // Category nula
                product.brand(),
                product.specifications()
        );

        // When
        ProductDto dto = productMapper.toDto(productWithNullCategory);

        // Then
        assertNotNull(dto);
        assertNull(dto.category());
    }

    @Test
    @DisplayName("Dado un Product dominio con specifications vacías, cuando se mapea a ProductDto, entonces las specifications deben estar vacías")
    void givenProductDomainWithEmptySpecifications_whenMappedToDto_thenSpecificationsAreEmpty() {
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
                new ProductSpecifications(Map.of()) // Specifications vacías
        );

        // When
        ProductDto dto = productMapper.toDto(productWithEmptySpecs);

        // Then
        assertNotNull(dto);
        assertNotNull(dto.specifications());
        assertTrue(dto.specifications().isEmpty());
    }

    @Test
    @DisplayName("Dado un CreateProductDto válido, cuando se mapea a Product dominio, entonces debe mapear correctamente todos los campos")
    void givenValidCreateProductDto_whenMappedToDomain_thenAllFieldsAreMappedCorrectly() {
        // Given
        CreateProductDto createProductDto = new CreateProductDto(
                "New Smartphone",
                "https://example.com/new-smartphone.jpg",
                "New smartphone description",
                new BigDecimal("699.99"),
                "EUR",
                4.5,
                "Electronics",
                "NewBrand",
                Map.of("screen", "6.1 inches", "memory", "128GB")
        );

        // When
        Product product = productMapper.toDomain(createProductDto);

        // Then
        assertNotNull(product);

        // ID debe ser generado automáticamente
        assertNotNull(product.id());
        assertNotNull(product.id().value());
        assertFalse(product.id().value().isEmpty());

        assertEquals("New Smartphone", product.name().value());
        assertEquals("https://example.com/new-smartphone.jpg", product.imageUrl().value());
        assertEquals("New smartphone description", product.description().value());
        assertEquals(new BigDecimal("699.99"), product.price().value());
        assertEquals(Currency.getInstance("EUR"), product.price().currency());
        assertEquals(4.5, product.rating().value());
        assertEquals("Electronics", product.category().value());
        assertEquals("NewBrand", product.brand().value());

        // Verificar especificaciones
        assertNotNull(product.specifications());
        assertEquals(2, product.specifications().specs().size());
        assertEquals("6.1 inches", product.specifications().specs().get("screen"));
        assertEquals("128GB", product.specifications().specs().get("memory"));
    }

    @Test
    @DisplayName("Dado un CreateProductDto con rating nulo, cuando se mapea a Product dominio, entonces el rating debe ser nulo")
    void givenCreateProductDtoWithNullRating_whenMappedToDomain_thenRatingIsNull() {
        // Given
        CreateProductDto createProductDto = new CreateProductDto(
                "Product Name",
                "https://example.com/image.jpg",
                "Description",
                new BigDecimal("100.00"),
                "USD",
                null, // Rating nulo
                "Electronics",
                "Brand",
                Map.of("key", "value")
        );

        // When
        Product product = productMapper.toDomain(createProductDto);

        // Then
        assertNotNull(product);
        assertNull(product.rating());
    }

    @Test
    @DisplayName("Dado un CreateProductDto con brand nulo, cuando se mapea a Product dominio, entonces el brand debe ser nulo")
    void givenCreateProductDtoWithNullBrand_whenMappedToDomain_thenBrandIsNull() {
        // Given
        CreateProductDto createProductDto = new CreateProductDto(
                "Product Name",
                "https://example.com/image.jpg",
                "Description",
                new BigDecimal("100.00"),
                "USD",
                4.5,
                "Electronics",
                null, // Brand nulo
                Map.of("key", "value")
        );

        // When
        Product product = productMapper.toDomain(createProductDto);

        // Then
        assertNotNull(product);
        assertNull(product.brand().value());
    }

    @Test
    @DisplayName("Dado un CreateProductDto con category nula, cuando se mapea a Product dominio, entonces la category debe ser nula")
    void givenCreateProductDtoWithNullCategory_whenMappedToDomain_thenCategoryIsNull() {
        // Given
        CreateProductDto createProductDto = new CreateProductDto(
                "Product Name",
                "https://example.com/image.jpg",
                "Description",
                new BigDecimal("100.00"),
                "USD",
                4.5,
                null, // Category nula
                "Brand",
                Map.of("key", "value")
        );

        // When
        Product product = productMapper.toDomain(createProductDto);

        // Then
        assertNotNull(product);
        assertNull(product.category().value());
    }

    @Test
    @DisplayName("Dado un CreateProductDto con specifications nulas, cuando se mapea a Product dominio, entonces las specifications deben estar vacías")
    void givenCreateProductDtoWithNullSpecifications_whenMappedToDomain_thenSpecificationsAreEmpty() {
        // Given
        CreateProductDto createProductDto = new CreateProductDto(
                "Product Name",
                "https://example.com/image.jpg",
                "Description",
                new BigDecimal("100.00"),
                "USD",
                4.5,
                "Electronics",
                "Brand",
                null // Specifications nulas
        );

        // When
        Product product = productMapper.toDomain(createProductDto);

        // Then
        assertNotNull(product);
        assertNotNull(product.specifications());
        assertNotNull(product.specifications().specs());
        assertTrue(product.specifications().specs().isEmpty());
    }

    @Test
    @DisplayName("Dado un CreateProductDto con specifications vacías, cuando se mapea a Product dominio, entonces las specifications deben estar vacías")
    void givenCreateProductDtoWithEmptySpecifications_whenMappedToDomain_thenSpecificationsAreEmpty() {
        // Given
        CreateProductDto createProductDto = new CreateProductDto(
                "Product Name",
                "https://example.com/image.jpg",
                "Description",
                new BigDecimal("100.00"),
                "USD",
                4.5,
                "Electronics",
                "Brand",
                Map.of() // Specifications vacías
        );

        // When
        Product product = productMapper.toDomain(createProductDto);

        // Then
        assertNotNull(product);
        assertNotNull(product.specifications());
        assertNotNull(product.specifications().specs());
        assertTrue(product.specifications().specs().isEmpty());
    }

    @Test
    @DisplayName("Dado un CreateProductDto con currency nula, cuando se mapea a Product dominio, entonces debe lanzar excepción")
    void givenCreateProductDtoWithNullCurrency_whenMappedToDomain_thenShouldThrowException() {
        // Given
        CreateProductDto createProductDto = new CreateProductDto(
                "Product Name",
                "https://example.com/image.jpg",
                "Description",
                new BigDecimal("100.00"),
                null, // Currency nula
                4.5,
                "Electronics",
                "Brand",
                Map.of("key", "value")
        );

        // When & Then
        assertThrows(NullPointerException.class,
                () -> productMapper.toDomain(createProductDto));
    }

    @Test
    @DisplayName("Dado un ID y UpdateProductDto válido, cuando se mapea a Product dominio, entonces debe mapear correctamente todos los campos")
    void givenValidIdAndUpdateProductDto_whenMappedToDomain_thenAllFieldsAreMappedCorrectly() {
        // Given
        String productId = "prod123";
        UpdateProductDto updateProductDto = new UpdateProductDto(
                "Updated Name",
                "https://example.com/updated-image.jpg",
                "Updated description",
                new BigDecimal("799.99"),
                "EUR",
                4.8,
                "Updated Category",
                "Updated Brand",
                Map.of("new_spec", "new_value")
        );

        // When
        Product product = productMapper.toDomain(productId, updateProductDto);

        // Then
        assertNotNull(product);
        assertEquals(productId, product.id().value());
        assertEquals("Updated Name", product.name().value());
        assertEquals("https://example.com/updated-image.jpg", product.imageUrl().value());
        assertEquals("Updated description", product.description().value());
        assertEquals(new BigDecimal("799.99"), product.price().value());
        assertEquals(Currency.getInstance("EUR"), product.price().currency());
        assertEquals(4.8, product.rating().value());
        assertEquals("Updated Category", product.category().value());
        assertEquals("Updated Brand", product.brand().value());
        assertEquals("new_value", product.specifications().specs().get("new_spec"));
    }

    @Test
    @DisplayName("Dado un UpdateProductDto con currency nula, cuando se mapea a Product dominio, entonces debe lanzar excepción")
    void givenUpdateProductDtoWithNullCurrency_whenMappedToDomain_thenShouldThrowException() {
        // Given
        String productId = "prod123";
        UpdateProductDto updateProductDto = new UpdateProductDto(
                "Name",
                "https://example.com/image.jpg",
                "Description",
                new BigDecimal("100.00"),
                null, // Currency nula
                4.5,
                "Electronics",
                "Brand",
                Map.of("key", "value")
        );

        // When & Then
        assertThrows(NullPointerException.class,
                () -> productMapper.toDomain(productId, updateProductDto));
    }

    @Test
    @DisplayName("Dado un UpdateProductDto con rating nulo, cuando se mapea a Product dominio, entonces el rating debe ser nulo")
    void givenUpdateProductDtoWithNullRating_whenMappedToDomain_thenRatingIsNull() {
        // Given
        String productId = "prod123";
        UpdateProductDto updateProductDto = new UpdateProductDto(
                "Name",
                "https://example.com/image.jpg",
                "Description",
                new BigDecimal("100.00"),
                "USD",
                null, // Rating nulo
                "Electronics",
                "Brand",
                Map.of("key", "value")
        );

        // When
        Product product = productMapper.toDomain(productId, updateProductDto);

        // Then
        assertNotNull(product);
        assertNull(product.rating());
    }

    @Test
    @DisplayName("Dado un UpdateProductDto con specifications nulas, cuando se mapea a Product dominio, entonces las specifications deben estar vacías")
    void givenUpdateProductDtoWithNullSpecifications_whenMappedToDomain_thenSpecificationsAreEmpty() {
        // Given
        String productId = "prod123";
        UpdateProductDto updateProductDto = new UpdateProductDto(
                "Name",
                "https://example.com/image.jpg",
                "Description",
                new BigDecimal("100.00"),
                "USD",
                4.5,
                "Electronics",
                "Brand",
                null // Specifications nulas
        );

        // When
        Product product = productMapper.toDomain(productId, updateProductDto);

        // Then
        assertNotNull(product);
        assertNotNull(product.specifications());
        assertNotNull(product.specifications().specs());
        assertTrue(product.specifications().specs().isEmpty());
    }

    @Test
    @DisplayName("Dado diferentes productos del Object Mother, cuando se mapean a DTO y viceversa, entonces deben mantener la consistencia")
    void givenDifferentProductsFromObjectMother_whenMappedBothWays_thenShouldMaintainConsistency() {
        // Given
        Product smartphone = ProductObjectMother.smartphoneGalaxyXZ();
        Product laptop = ProductObjectMother.laptopPro();

        // When: Product -> DTO
        ProductDto smartphoneDto = productMapper.toDto(smartphone);
        ProductDto laptopDto = productMapper.toDto(laptop);

        // Then: Verificar mapeo correcto
        assertEquals(smartphone.id().value(), smartphoneDto.id());
        assertEquals(smartphone.name().value(), smartphoneDto.name());
        assertEquals(laptop.id().value(), laptopDto.id());
        assertEquals(laptop.name().value(), laptopDto.name());
    }

    @Test
    @DisplayName("Dado un CreateProductDto, cuando se mapea múltiples veces, entonces cada producto debe tener un ID único")
    void givenCreateProductDto_whenMappedMultipleTimes_thenEachProductShouldHaveUniqueId() {
        // Given
        CreateProductDto createProductDto = new CreateProductDto(
                "Product Name",
                "https://example.com/image.jpg",
                "Description",
                new BigDecimal("100.00"),
                "USD",
                4.5,
                "Electronics",
                "Brand",
                Map.of("key", "value")
        );

        // When
        Product product1 = productMapper.toDomain(createProductDto);
        Product product2 = productMapper.toDomain(createProductDto);
        Product product3 = productMapper.toDomain(createProductDto);

        // Then
        assertNotNull(product1.id().value());
        assertNotNull(product2.id().value());
        assertNotNull(product3.id().value());

        // Los IDs deben ser diferentes (UUID generados)
        assertNotEquals(product1.id().value(), product2.id().value());
        assertNotEquals(product1.id().value(), product3.id().value());
        assertNotEquals(product2.id().value(), product3.id().value());

        // Los IDs deben ser UUID válidos
        assertDoesNotThrow(() -> UUID.fromString(product1.id().value()));
        assertDoesNotThrow(() -> UUID.fromString(product2.id().value()));
        assertDoesNotThrow(() -> UUID.fromString(product3.id().value()));
    }

    @Test
    @DisplayName("Dado un Product nulo en toDto, cuando se mapea, entonces debe retornar null")
    void givenNullProductInToDto_whenMapped_thenShouldReturnNull() {
        // When
        ProductDto result = productMapper.toDto(null);

        // Then
        assertNull(result);
    }

    @Test
    @DisplayName("Dado un CreateProductDto nulo en toDomain, cuando se mapea, entonces debe lanzar ProductMapperException")
    void givenNullCreateProductDtoInToDomain_whenMapped_thenShouldThrowProductMapperException() {
        // When & Then
        ProductMapperException exception = assertThrows(ProductMapperException.class,
                () -> productMapper.toDomain((CreateProductDto) null));

        assertEquals("CreateProductDto cannot be null", exception.getMessage());
    }

    @Test
    @DisplayName("Dado un UpdateProductDto nulo en toDomain, cuando se mapea, entonces debe lanzar ProductMapperException")
    void givenNullUpdateProductDtoInToDomain_whenMapped_thenShouldThrowProductMapperException() {
        // Given
        String productId = "prod123";

        // When & Then
        ProductMapperException exception = assertThrows(ProductMapperException.class,
                () -> productMapper.toDomain(productId, null));

        assertEquals("UpdateProductDto cannot be null", exception.getMessage());
    }

    @Test
    @DisplayName("Dado un UpdateProductDto con specifications nulas, cuando se mapea, entonces debe usar Map.of() por defecto")
    void givenUpdateProductDtoWithNullSpecifications_whenMappedToDomain_thenShouldUseEmptyMap() {
        // Given
        String productId = "prod123";
        UpdateProductDto updateProductDto = new UpdateProductDto(
                "Name",
                "https://example.com/image.jpg",
                "Description",
                new BigDecimal("100.00"),
                "USD",
                4.5,
                "Electronics",
                "Brand",
                null // Specifications nulas
        );

        // When
        Product product = productMapper.toDomain(productId, updateProductDto);

        // Then
        assertNotNull(product.specifications());
        assertNotNull(product.specifications().specs());
        assertTrue(product.specifications().specs().isEmpty());
    }

    @Test
    @DisplayName("Dado un Product con todos los campos, cuando se mapea a DTO, entonces debe mantener la integridad de los datos")
    void givenCompleteProduct_whenMappedToDto_thenShouldMaintainDataIntegrity() {
        // Given
        Product product = ProductObjectMother.laptopPro();

        // When
        ProductDto dto = productMapper.toDto(product);

        // Then
        assertEquals("prod002", dto.id());
        assertEquals("Laptop Pro Ultra", dto.name());
        assertEquals("https://example.com/laptop-pro.jpg", dto.imageUrl());
        assertEquals("Laptop profesional para trabajo intensivo", dto.description());
        assertEquals(new BigDecimal("1299.99"), dto.price());
        assertEquals("USD", dto.currency());
        assertEquals(4.5, dto.rating());
        assertEquals("Computadoras", dto.category());
        assertEquals("TechMaster", dto.brand());

        // Verificar especificaciones de laptop
        assertEquals("14 pulgadas Retina", dto.specifications().get("pantalla"));
        assertEquals("Intel Core i7", dto.specifications().get("procesador"));
        assertEquals("16GB RAM", dto.specifications().get("memoria"));
    }
}
