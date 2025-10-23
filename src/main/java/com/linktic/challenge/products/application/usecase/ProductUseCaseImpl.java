package com.linktic.challenge.products.application.usecase;

import com.linktic.challenge.products.application.port.in.ProductManagementUseCase;
import com.linktic.challenge.products.application.port.in.ProductQueryUseCase;
import com.linktic.challenge.products.domain.exception.entity.ProductNotFoundException;
import com.linktic.challenge.products.domain.model.Product;
import com.linktic.challenge.products.domain.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductUseCaseImpl implements ProductQueryUseCase, ProductManagementUseCase {
    private final ProductRepository productRepository;

    @Override
    public Product findById(String id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));
    }

    @Override
    public Page<Product> findAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    @Override
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public Product updateProduct(Product product) {
        return productRepository.update(product);
    }

    @Override
    public void deleteProduct(String id) {
        productRepository.deleteById(id);
    }
}
