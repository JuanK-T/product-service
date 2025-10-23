package com.linktic.challenge.products.infrastructure.adapter;

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
        ProductEntity entity = productMapper.toEntity(product);
        ProductEntity savedEntity = productJpaRepository.save(entity);
        return productMapper.toDomain(savedEntity);
    }

    @Override
    public Product update(Product product) {
        // Verificar si el producto existe
        if (!productJpaRepository.existsById(product.id().value())) {
            throw new ProductNotFoundException("Product not found with id: " + product.id().value());
        }

        // Buscar la entidad existente
        ProductEntity existingEntity = productJpaRepository.findById(product.id().value())
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + product.id().value()));

        // Actualizar la entidad existente con los nuevos datos
        productMapper.updateEntityFromDomain(product, existingEntity);

        // Guardar la entidad actualizada
        ProductEntity updatedEntity = productJpaRepository.save(existingEntity);
        return productMapper.toDomain(updatedEntity);
    }

    @Override
    public void deleteById(String id) {
        if (!productJpaRepository.existsById(id)) {
            throw new ProductNotFoundException("Product not found with id: " + id);
        }
        productJpaRepository.deleteById(id);
    }

    @Override
    public boolean existsById(String id) {
        return productJpaRepository.existsById(id);
    }
}
