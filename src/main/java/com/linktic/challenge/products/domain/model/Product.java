package com.linktic.challenge.products.domain.model;

public record Product(
        ProductId id,
        ProductName name,
        ProductImageUrl imageUrl,
        ProductDescription description,
        ProductPrice price,
        ProductRating rating,
        ProductCategory category,
        ProductBrand brand,
        ProductSpecifications specifications
) { }
