package com.linktic.challenge.products.application.mapper;

import com.linktic.challenge.products.application.dto.CreateProductDto;
import com.linktic.challenge.products.application.dto.ProductDto;
import com.linktic.challenge.products.application.dto.UpdateProductDto;
import com.linktic.challenge.products.domain.exception.mapper.ProductMapperException;
import com.linktic.challenge.products.domain.model.*;
import org.springframework.stereotype.Component;

import java.util.Currency;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Component
public class ProductMapper {

    public ProductDto toDto(Product product) {
        if (product == null) {
            return null;
        }

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
        if (createProductDto == null) {
            throw new ProductMapperException("CreateProductDto cannot be null");
        }

        return new Product(
                new ProductId(UUID.randomUUID().toString()),
                new ProductName(createProductDto.name()),
                new ProductImageUrl(createProductDto.imageUrl()),
                new ProductDescription(createProductDto.description()),
                new ProductPrice(
                        createProductDto.price(),
                        Currency.getInstance(Objects.requireNonNull(createProductDto.currency(), "currency required"))
                ),
                createProductDto.rating() != null ? ProductRating.of(createProductDto.rating()) : null,
                new ProductCategory(createProductDto.category()),
                new ProductBrand(createProductDto.brand()),
                new ProductSpecifications(
                        createProductDto.specifications() != null ?
                                createProductDto.specifications() : Map.of()
                )
        );
    }

    public Product toDomain(String id, UpdateProductDto updateProductDto) {
        if (updateProductDto == null) {
            throw new ProductMapperException("UpdateProductDto cannot be null");
        }

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