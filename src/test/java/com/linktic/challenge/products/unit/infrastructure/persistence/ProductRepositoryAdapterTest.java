package com.linktic.challenge.products.unit.infrastructure.persistence;

import com.linktic.challenge.products.domain.exception.entity.ProductAlreadyExistsException;
import com.linktic.challenge.products.domain.exception.entity.ProductNotFoundException;
import com.linktic.challenge.products.domain.model.Product;
import com.linktic.challenge.products.domain.model.ProductName;
import com.linktic.challenge.products.infrastructure.adapter.ProductRepositoryAdapter;
import com.linktic.challenge.products.infrastructure.persistence.entity.ProductEntity;
import com.linktic.challenge.products.infrastructure.persistence.mapper.ProductEntityMapper;
import com.linktic.challenge.products.infrastructure.persistence.repository.ProductJpaRepository;
import com.linktic.challenge.products.objectmother.ProductObjectMother;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductRepositoryAdapterTest {

    @Mock
    private ProductJpaRepository productJpaRepository;

    @Mock
    private ProductEntityMapper productMapper;

    @InjectMocks
    private ProductRepositoryAdapter productRepositoryAdapter;

    private Product product;
    private ProductEntity productEntity;

    @BeforeEach
    void setUp() {
        product = ProductObjectMother.smartphoneGalaxyXZ();
        productEntity = createProductEntity();
    }

    @Test
    @DisplayName("Dado un ID existente, cuando se busca por ID, entonces debe retornar el producto")
    void givenExistingId_whenFindById_thenShouldReturnProduct() {
        // Given
        String productId = "prod001";
        when(productJpaRepository.findById(productId)).thenReturn(Optional.of(productEntity));
        when(productMapper.toDomain(productEntity)).thenReturn(product);

        // When
        Optional<Product> result = productRepositoryAdapter.findById(productId);

        // Then
        assertTrue(result.isPresent());
        assertEquals(product, result.get());
        verify(productJpaRepository).findById(productId);
        verify(productMapper).toDomain(productEntity);
    }

    @Test
    @DisplayName("Dado un ID no existente, cuando se busca por ID, entonces debe retornar Optional vacío")
    void givenNonExistingId_whenFindById_thenShouldReturnEmptyOptional() {
        // Given
        String productId = "non-existing";
        when(productJpaRepository.findById(productId)).thenReturn(Optional.empty());

        // When
        Optional<Product> result = productRepositoryAdapter.findById(productId);

        // Then
        assertFalse(result.isPresent());
        verify(productJpaRepository).findById(productId);
        verify(productMapper, never()).toDomain(any());
    }

    @Test
    @DisplayName("Dado un pageable, cuando se buscan todos los productos, entonces debe retornar página de productos")
    void givenPageable_whenFindAll_thenShouldReturnPageOfProducts() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        List<ProductEntity> entities = List.of(productEntity, createLaptopEntity());
        Page<ProductEntity> entityPage = new PageImpl<>(entities, pageable, entities.size());

        Product laptop = ProductObjectMother.laptopPro();
        when(productJpaRepository.findAll(pageable)).thenReturn(entityPage);
        when(productMapper.toDomain(any(ProductEntity.class))).thenReturn(laptop, product);

        // When
        Page<Product> result = productRepositoryAdapter.findAll(pageable);

        // Then
        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        assertTrue(result.getContent().contains(product));
        verify(productJpaRepository).findAll(pageable);
        verify(productMapper, times(2)).toDomain(any(ProductEntity.class));
    }

    @Test
    @DisplayName("Dado un producto válido, cuando se guarda, entonces debe retornar el producto guardado")
    void givenValidProduct_whenSave_thenShouldReturnSavedProduct() {
        // Given
        ProductEntity savedEntity = createProductEntity();
        String productNameValue = product.name().value();

        when(productJpaRepository.existsByName(productNameValue)).thenReturn(false);
        when(productMapper.toEntity(product)).thenReturn(productEntity);
        when(productJpaRepository.save(productEntity)).thenReturn(savedEntity);
        when(productMapper.toDomain(savedEntity)).thenReturn(product);

        // When
        Product result = productRepositoryAdapter.save(product);

        // Then
        assertNotNull(result);
        assertEquals(product, result);
        verify(productJpaRepository).existsByName(productNameValue);
        verify(productMapper).toEntity(product);
        verify(productJpaRepository).save(productEntity);
        verify(productMapper).toDomain(savedEntity);
    }

    @Test
    @DisplayName("Dado un producto con nombre existente, cuando se guarda, entonces debe lanzar ProductAlreadyExistsException")
    void givenProductWithExistingName_whenSave_thenShouldThrowProductAlreadyExistsException() {
        // Given
        String productNameValue = product.name().value();
        when(productJpaRepository.existsByName(productNameValue)).thenReturn(true);

        // When & Then
        ProductAlreadyExistsException exception = assertThrows(ProductAlreadyExistsException.class,
                () -> productRepositoryAdapter.save(product));

        assertEquals("Product already exists with name: " + productNameValue, exception.getMessage());
        verify(productJpaRepository).existsByName(productNameValue);
        verify(productJpaRepository, never()).save(any());
        verify(productMapper, never()).toEntity(any());
    }

    @Test
    @DisplayName("Dado un producto existente, cuando se actualiza, entonces debe retornar el producto actualizado")
    void givenExistingProduct_whenUpdate_thenShouldReturnUpdatedProduct() {
        // Given
        String productId = product.id().value();

        // ✅ Crear producto con nombre DIFERENTE
        String differentName = "Updated Smartphone Name";
        Product productWithDifferentName = new Product(
                product.id(),
                new ProductName(differentName),
                product.imageUrl(),
                product.description(),
                product.price(),
                product.rating(),
                product.category(),
                product.brand(),
                product.specifications()
        );

        String productNameValue = differentName;
        ProductEntity updatedEntity = createProductEntity();

        when(productJpaRepository.findById(productId)).thenReturn(Optional.of(productEntity));
        when(productJpaRepository.existsByNameAndIdNot(productNameValue, productId)).thenReturn(false);
        when(productJpaRepository.save(productEntity)).thenReturn(updatedEntity);
        when(productMapper.toDomain(updatedEntity)).thenReturn(productWithDifferentName);

        // When
        Product result = productRepositoryAdapter.update(productWithDifferentName);

        // Then
        assertNotNull(result);
        assertEquals(productWithDifferentName, result);
        verify(productJpaRepository).findById(productId);
        verify(productJpaRepository).existsByNameAndIdNot(productNameValue, productId);
        verify(productMapper).updateEntityFromDomain(productWithDifferentName, productEntity);
        verify(productJpaRepository).save(productEntity);
        verify(productMapper).toDomain(updatedEntity);
    }

    @Test
    @DisplayName("Dado un producto no existente, cuando se intenta actualizar, entonces debe lanzar ProductNotFoundException")
    void givenNonExistingProduct_whenUpdate_thenShouldThrowProductNotFoundException() {
        // Given
        String productId = product.id().value();
        when(productJpaRepository.findById(productId)).thenReturn(Optional.empty());

        // When & Then
        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class,
                () -> productRepositoryAdapter.update(product));

        assertEquals("Product not found with ID: " + productId, exception.getMessage());
        verify(productJpaRepository).findById(productId);
        verify(productJpaRepository, never()).existsByNameAndIdNot(anyString(), anyString());
        verify(productMapper, never()).updateEntityFromDomain(any(), any());
        verify(productJpaRepository, never()).save(any());
    }

    @Test
    @DisplayName("Dado un producto con ID nulo, cuando se intenta actualizar, entonces debe lanzar ProductNotFoundException")
    void givenProductWithNullId_whenUpdate_thenShouldThrowProductNotFoundException() {
        // Given
        Product productWithNullId = new Product(
                null, // ID nulo
                product.name(),
                product.imageUrl(),
                product.description(),
                product.price(),
                product.rating(),
                product.category(),
                product.brand(),
                product.specifications()
        );

        // When & Then
        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class,
                () -> productRepositoryAdapter.update(productWithNullId));

        assertEquals("Product not found with ID: null", exception.getMessage());
        verify(productJpaRepository, never()).findById(anyString());
        verify(productJpaRepository, never()).existsByNameAndIdNot(anyString(), anyString());
        verify(productMapper, never()).updateEntityFromDomain(any(), any());
        verify(productJpaRepository, never()).save(any());
    }

    @Test
    @DisplayName("Dado un producto actualizado con nombre existente en otro producto, cuando se actualiza, entonces debe lanzar ProductAlreadyExistsException")
    void givenProductWithExistingNameInOtherProduct_whenUpdate_thenShouldThrowProductAlreadyExistsException() {
        // Given
        String productId = product.id().value();
        String existingName = "Existing Product Name"; // Este nombre cumple con las validaciones (4-60 caracteres)
        Product productWithExistingName = new Product(
                product.id(),
                new ProductName(existingName), // ✅ Usar el constructor del record
                product.imageUrl(),
                product.description(),
                product.price(),
                product.rating(),
                product.category(),
                product.brand(),
                product.specifications()
        );

        when(productJpaRepository.findById(productId)).thenReturn(Optional.of(productEntity));
        when(productJpaRepository.existsByNameAndIdNot(existingName, productId)).thenReturn(true);

        // When & Then
        ProductAlreadyExistsException exception = assertThrows(ProductAlreadyExistsException.class,
                () -> productRepositoryAdapter.update(productWithExistingName));

        assertEquals("Product already exists with name: " + existingName, exception.getMessage());
        verify(productJpaRepository).findById(productId);
        verify(productJpaRepository).existsByNameAndIdNot(existingName, productId);
        verify(productMapper, never()).updateEntityFromDomain(any(), any());
        verify(productJpaRepository, never()).save(any());
    }

    @Test
    @DisplayName("Dado un ID existente, cuando se elimina, entonces debe eliminar el producto")
    void givenExistingId_whenDeleteById_thenShouldDeleteProduct() {
        // Given
        String productId = "prod001";
        when(productJpaRepository.existsById(productId)).thenReturn(true);

        // When
        productRepositoryAdapter.deleteById(productId);

        // Then
        verify(productJpaRepository).existsById(productId);
        verify(productJpaRepository).deleteById(productId);
    }

    @Test
    @DisplayName("Dado un ID no existente, cuando se intenta eliminar, entonces debe lanzar ProductNotFoundException")
    void givenNonExistingId_whenDeleteById_thenShouldThrowProductNotFoundException() {
        // Given
        String productId = "non-existing";
        when(productJpaRepository.existsById(productId)).thenReturn(false);

        // When & Then
        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class,
                () -> productRepositoryAdapter.deleteById(productId));

        assertEquals("Product not found with ID: " + productId, exception.getMessage());
        verify(productJpaRepository).existsById(productId);
        verify(productJpaRepository, never()).deleteById(anyString());
    }

    @Test
    @DisplayName("Dado un ID existente, cuando se verifica existencia, entonces debe retornar true")
    void givenExistingId_whenExistsById_thenShouldReturnTrue() {
        // Given
        String productId = "prod001";
        when(productJpaRepository.existsById(productId)).thenReturn(true);

        // When
        boolean result = productRepositoryAdapter.existsById(productId);

        // Then
        assertTrue(result);
        verify(productJpaRepository).existsById(productId);
    }

    @Test
    @DisplayName("Dado un ID no existente, cuando se verifica existencia, entonces debe retornar false")
    void givenNonExistingId_whenExistsById_thenShouldReturnFalse() {
        // Given
        String productId = "non-existing";
        when(productJpaRepository.existsById(productId)).thenReturn(false);

        // When
        boolean result = productRepositoryAdapter.existsById(productId);

        // Then
        assertFalse(result);
        verify(productJpaRepository).existsById(productId);
    }

    @Test
    @DisplayName("Dado una página vacía, cuando se buscan todos los productos, entonces debe retornar página vacía")
    void givenEmptyPage_whenFindAll_thenShouldReturnEmptyPage() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        Page<ProductEntity> emptyPage = Page.empty();
        when(productJpaRepository.findAll(pageable)).thenReturn(emptyPage);

        // When
        Page<Product> result = productRepositoryAdapter.findAll(pageable);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(productJpaRepository).findAll(pageable);
        verify(productMapper, never()).toDomain(any());
    }

    @Test
    @DisplayName("Dado un producto actualizado, cuando el mapper falla, entonces debe propagar la excepción")
    void givenProductUpdate_whenMapperFails_thenShouldPropagateException() {
        // Given
        String productId = product.id().value();

        // ✅ Crear producto con nombre DIFERENTE
        String differentName = "Updated Product Name";
        Product productWithDifferentName = new Product(
                product.id(),
                new ProductName(differentName),
                product.imageUrl(),
                product.description(),
                product.price(),
                product.rating(),
                product.category(),
                product.brand(),
                product.specifications()
        );

        String productNameValue = differentName;

        when(productJpaRepository.findById(productId)).thenReturn(Optional.of(productEntity));
        when(productJpaRepository.existsByNameAndIdNot(productNameValue, productId)).thenReturn(false);
        doThrow(new RuntimeException("Mapping error"))
                .when(productMapper).updateEntityFromDomain(productWithDifferentName, productEntity);

        // When & Then
        assertThrows(RuntimeException.class,
                () -> productRepositoryAdapter.update(productWithDifferentName));

        verify(productJpaRepository).findById(productId);
        verify(productJpaRepository).existsByNameAndIdNot(productNameValue, productId);
        verify(productMapper).updateEntityFromDomain(productWithDifferentName, productEntity);
        verify(productJpaRepository, never()).save(any());
    }

    @Test
    @DisplayName("Dado un producto a guardar, cuando el repositorio falla, entonces debe propagar la excepción")
    void givenProductToSave_whenRepositoryFails_thenShouldPropagateException() {
        // Given
        String productNameValue = product.name().value();

        when(productJpaRepository.existsByName(productNameValue)).thenReturn(false);
        when(productMapper.toEntity(product)).thenReturn(productEntity);
        when(productJpaRepository.save(productEntity)).thenThrow(new RuntimeException("Database error"));

        // When & Then
        assertThrows(RuntimeException.class,
                () -> productRepositoryAdapter.save(product));

        verify(productJpaRepository).existsByName(productNameValue);
        verify(productMapper).toEntity(product);
        verify(productJpaRepository).save(productEntity);
        verify(productMapper, never()).toDomain(any());
    }

    @Test
    @DisplayName("Dado múltiples productos, cuando se buscan con paginación, entonces debe mapear correctamente todos")
    void givenMultipleProducts_whenFindAllWithPagination_thenShouldMapAllCorrectly() {
        // Given
        Pageable pageable = PageRequest.of(0, 5);
        List<ProductEntity> entities = List.of(
                createProductEntity(),
                createLaptopEntity(),
                createProductEntity()
        );
        Page<ProductEntity> entityPage = new PageImpl<>(entities, pageable, entities.size());

        when(productJpaRepository.findAll(pageable)).thenReturn(entityPage);
        when(productMapper.toDomain(any(ProductEntity.class))).thenReturn(product);

        // When
        Page<Product> result = productRepositoryAdapter.findAll(pageable);

        // Then
        assertNotNull(result);
        assertEquals(3, result.getContent().size());
        verify(productJpaRepository).findAll(pageable);
        verify(productMapper, times(3)).toDomain(any(ProductEntity.class));
    }

    @Test
    @DisplayName("Dado un producto a actualizar, cuando el guardado falla, entonces debe propagar la excepción")
    void givenProductToUpdate_whenSaveFails_thenShouldPropagateException() {
        // Given
        String productId = product.id().value();

        // ✅ Crear un producto con nombre DIFERENTE para forzar la validación de duplicados
        String differentName = "Different Product Name";
        Product productWithDifferentName = new Product(
                product.id(),
                new ProductName(differentName),
                product.imageUrl(),
                product.description(),
                product.price(),
                product.rating(),
                product.category(),
                product.brand(),
                product.specifications()
        );

        String productNameValue = differentName; // Usar el nombre diferente

        when(productJpaRepository.findById(productId)).thenReturn(Optional.of(productEntity));
        when(productJpaRepository.existsByNameAndIdNot(productNameValue, productId)).thenReturn(false);
        when(productJpaRepository.save(productEntity)).thenThrow(new RuntimeException("Save failed"));

        // When & Then
        assertThrows(RuntimeException.class,
                () -> productRepositoryAdapter.update(productWithDifferentName));

        verify(productJpaRepository).findById(productId);
        verify(productJpaRepository).existsByNameAndIdNot(productNameValue, productId);
        verify(productMapper).updateEntityFromDomain(productWithDifferentName, productEntity);
        verify(productJpaRepository).save(productEntity);
        verify(productMapper, never()).toDomain(any());
    }

    // Métodos helper
    private ProductEntity createProductEntity() {
        return ProductEntity.builder()
                .id("prod001")
                .name("Smartphone Galaxy XZ")
                .imageUrl("https://example.com/galaxy-xz.jpg")
                .description("Smartphone flagship con cámara profesional")
                .price(new java.math.BigDecimal("899.99"))
                .currency("USD")
                .rating(4.7)
                .category("Electrónica")
                .brand("TechNova")
                .specifications(new java.util.ArrayList<>())
                .build();
    }

    private ProductEntity createLaptopEntity() {
        return ProductEntity.builder()
                .id("prod002")
                .name("Laptop Pro Ultra")
                .imageUrl("https://example.com/laptop-pro.jpg")
                .description("Laptop profesional para trabajo intensivo")
                .price(new java.math.BigDecimal("1299.99"))
                .currency("USD")
                .rating(4.5)
                .category("Computadoras")
                .brand("TechMaster")
                .specifications(new java.util.ArrayList<>())
                .build();
    }
}