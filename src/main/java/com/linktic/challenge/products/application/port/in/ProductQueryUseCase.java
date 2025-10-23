package com.linktic.challenge.products.application.port.in;

import com.linktic.challenge.products.domain.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductQueryUseCase {
    Product findById(String id);
    Page<Product> findAllProducts(Pageable pageable);
}
