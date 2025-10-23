package com.linktic.challenge.products.application.port.in;


import com.linktic.challenge.products.domain.model.Product;

public interface ProductManagementUseCase {
    Product createProduct(Product product);
    Product updateProduct(Product product);
    void deleteProduct(String id);
}
