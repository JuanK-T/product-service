package com.linktic.challenge.products.application.mapper;

import com.linktic.challenge.products.application.dto.CreateProductDto;
import com.linktic.challenge.products.application.dto.ProductDto;
import com.linktic.challenge.products.application.dto.UpdateProductDto;
import com.linktic.challenge.products.domain.model.*;
import org.springframework.stereotype.Component;

import java.util.Currency;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Component
public class ProductMapper {

    public ProductDto toDto(Product product) {
        return new ProductDto(
                product.id().value(),
                product.name().value(),
                product.imageUrl().value(),
                product.description().value(),
                product.price().value(),
                product.price().currency().getCurrencyCode(),
                product.rating() != null ? product.rating().value() : null,
                product.category().value(),
                product.brand().value(),
                product.specifications().specs()
        );
    }

    public Product toDomain(CreateProductDto createProductDto) {
        return new Product(
                new ProductId(UUID.randomUUID().toString()),
                new ProductName(createProductDto.getName()),
                new ProductImageUrl(createProductDto.getImageUrl()),
                new ProductDescription(createProductDto.getDescription()),
                new ProductPrice(
                        createProductDto.getPrice(),
                        Currency.getInstance(Objects.requireNonNull(createProductDto.getCurrency(), "currency required"))
                ),
                createProductDto.getRating() != null ? ProductRating.of(createProductDto.getRating()) : null,
                new ProductCategory(createProductDto.getCategory()),
                new ProductBrand(createProductDto.getBrand()),
                new ProductSpecifications(
                        createProductDto.getSpecifications() != null ?
                                createProductDto.getSpecifications() : Map.of()
                )
        );
    }

    public Product toDomain(String id, UpdateProductDto updateProductDto) {
        return new Product(
                new ProductId(id), // ID del path, no del DTO
                new ProductName(updateProductDto.name()),
                new ProductImageUrl(updateProductDto.imageUrl()),
                new ProductDescription(updateProductDto.description()),
                new ProductPrice(
                        updateProductDto.price(),
                        Currency.getInstance(Objects.requireNonNull(updateProductDto.currency(), "currency required"))
                ),
                updateProductDto.rating() != null ? ProductRating.of(updateProductDto.rating()) : null,
                new ProductCategory(updateProductDto.category()),
                new ProductBrand(updateProductDto.brand()),
                new ProductSpecifications(updateProductDto.specifications())
        );
    }
}