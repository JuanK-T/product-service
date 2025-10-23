package com.linktic.challenge.products.domain.repository;

import com.linktic.challenge.products.domain.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ProductRepository {

    Optional<Product> findById(String id);

    Page<Product> findAll(Pageable pageable);

    Product save(Product product);

    Product update(Product product);

    void deleteById(String id);

    boolean existsById(String id);
}