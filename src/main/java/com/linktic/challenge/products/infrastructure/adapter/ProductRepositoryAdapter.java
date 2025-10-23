package com.linktic.challenge.products.infrastructure.adapter;

import com.linktic.challenge.products.domain.exception.entity.ProductAlreadyExistsException;
import com.linktic.challenge.products.domain.exception.entity.ProductNotFoundException;
import com.linktic.challenge.products.domain.model.Product;
import com.linktic.challenge.products.domain.repository.ProductRepository;
import com.linktic.challenge.products.infrastructure.persistence.entity.ProductEntity;
import com.linktic.challenge.products.infrastructure.persistence.mapper.ProductEntityMapper;
import com.linktic.challenge.products.infrastructure.persistence.repository.ProductJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryAdapter implements ProductRepository {
    private final ProductJpaRepository productJpaRepository;
    private final ProductEntityMapper productMapper;

    @Override
    public Optional<Product> findById(String id) {
        return productJpaRepository.findById(id)
                .map(productMapper::toDomain);
    }

    @Override
    public Page<Product> findAll(Pageable pageable) {
        return productJpaRepository.findAll(pageable)
                .map(productMapper::toDomain);
    }

    @Override
    public Product save(Product product) {
        // ✅ Extraer el valor del Value Object ProductName
        String productNameValue = product.name().value();

        // Validar que no exista un producto con el mismo nombre
        if (productJpaRepository.existsByName(productNameValue)) {
            throw new ProductAlreadyExistsException(productNameValue);
        }

        ProductEntity entity = productMapper.toEntity(product);
        ProductEntity savedEntity = productJpaRepository.save(entity);
        return productMapper.toDomain(savedEntity);
    }

    @Override
    public Product update(Product product) {
        // Validar primero que el ID no sea nulo
        if (product.id() == null) {
            throw new ProductNotFoundException("null");
        }

        String productId = product.id().value();
        String productNameValue = product.name().value(); // ✅ Extraer valor

        // Buscar directamente la entidad existente
        ProductEntity existingEntity = productJpaRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        // Validar que el nuevo nombre no exista en otros productos
        if (!existingEntity.getName().equals(productNameValue) &&
                productJpaRepository.existsByNameAndIdNot(productNameValue, productId)) {
            throw new ProductAlreadyExistsException(productNameValue); // ✅ Pasar String
        }

        // Actualizar la entidad existente
        productMapper.updateEntityFromDomain(product, existingEntity);

        // Guardar la entidad actualizada
        ProductEntity updatedEntity = productJpaRepository.save(existingEntity);
        return productMapper.toDomain(updatedEntity);
    }

    @Override
    public void deleteById(String id) {
        if (!productJpaRepository.existsById(id)) {
            throw new ProductNotFoundException(id);
        }
        productJpaRepository.deleteById(id);
    }

    @Override
    public boolean existsById(String id) {
        return productJpaRepository.existsById(id);
    }
}
