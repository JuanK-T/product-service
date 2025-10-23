package com.linktic.challenge.products.infrastructure.persistence.mapper;

import com.linktic.challenge.products.domain.model.*;
import com.linktic.challenge.products.infrastructure.persistence.entity.ProductEntity;
import com.linktic.challenge.products.infrastructure.persistence.entity.ProductSpecificationEntity;
import org.mapstruct.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface ProductEntityMapper {

    // Domain to Entity
    @Mapping(target = "id", source = "id.value")
    @Mapping(target = "name", source = "name.value")
    @Mapping(target = "imageUrl", source = "imageUrl.value")
    @Mapping(target = "description", source = "description.value")
    @Mapping(target = "price", source = "price.value")
    @Mapping(target = "currency", source = "price.currency.currencyCode")
    @Mapping(target = "rating", source = "rating.value")
    @Mapping(target = "category", source = "category.value")
    @Mapping(target = "brand", source = "brand.value")
    @Mapping(target = "specifications", source = "specifications", qualifiedByName = "specsToEntities")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    ProductEntity toEntity(Product product);

    // Entity to Domain
    @Mapping(target = "id", expression = "java(mapId(entity.getId()))")
    @Mapping(target = "name", expression = "java(mapName(entity.getName()))")
    @Mapping(target = "imageUrl", expression = "java(mapImageUrl(entity.getImageUrl()))")
    @Mapping(target = "description", expression = "java(mapDescription(entity.getDescription()))")
    @Mapping(target = "price", expression = "java(mapPrice(entity.getPrice(), entity.getCurrency()))")
    @Mapping(target = "rating", expression = "java(mapRating(entity.getRating()))")
    @Mapping(target = "category", expression = "java(mapCategory(entity.getCategory()))")
    @Mapping(target = "brand", expression = "java(mapBrand(entity.getBrand()))")
    @Mapping(target = "specifications", expression = "java(mapSpecificationsFromEntities(entity.getSpecifications()))")
    Product toDomain(ProductEntity entity);

    // Update entity from domain
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", source = "name.value")
    @Mapping(target = "imageUrl", source = "imageUrl.value")
    @Mapping(target = "description", source = "description.value")
    @Mapping(target = "price", source = "price.value")
    @Mapping(target = "currency", source = "price.currency.currencyCode")
    @Mapping(target = "rating", source = "rating.value")
    @Mapping(target = "category", source = "category.value")
    @Mapping(target = "brand", source = "brand.value")
    @Mapping(target = "specifications", source = "specifications", qualifiedByName = "specsToEntities")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromDomain(Product product, @MappingTarget ProductEntity entity);

    // Named method for specifications conversion (Domain -> Entity)
    @Named("specsToEntities")
    default List<ProductSpecificationEntity> specsToEntities(ProductSpecifications specifications) {
        if (specifications == null || specifications.specs() == null || specifications.specs().isEmpty()) {
            return new ArrayList<>();
        }
        return specifications.specs().entrySet().stream()
                .map(entry -> ProductSpecificationEntity.builder()
                        .key(entry.getKey())
                        .value(entry.getValue())
                        .build())
                .toList();
    }

    // ✅ AFTER MAPPING QUE SÍ FUNCIONA - usando @Context
    @AfterMapping
    default void linkSpecificationsToProduct(@MappingTarget ProductEntity productEntity) {
        if (productEntity.getSpecifications() != null) {
            productEntity.getSpecifications().forEach(spec -> spec.setProduct(productEntity));
        }
    }

    // ✅ AFTER MAPPING para update también
    @AfterMapping
    default void linkSpecificationsToProductOnUpdate(@MappingTarget ProductEntity productEntity) {
        linkSpecificationsToProduct(productEntity);
    }

    // Method for specifications conversion (Entity -> Domain)
    default ProductSpecifications mapSpecificationsFromEntities(List<ProductSpecificationEntity> specifications) {
        if (specifications == null || specifications.isEmpty()) {
            return new ProductSpecifications(Map.of());
        }
        Map<String, String> specsMap = specifications.stream()
                .collect(Collectors.toMap(
                        ProductSpecificationEntity::getKey,
                        ProductSpecificationEntity::getValue
                ));
        return new ProductSpecifications(specsMap);
    }

    // Value object mapping methods
    default ProductId mapId(String id) {
        return new ProductId(id);
    }

    default ProductName mapName(String name) {
        return new ProductName(name);
    }

    default ProductImageUrl mapImageUrl(String imageUrl) {
        return new ProductImageUrl(imageUrl);
    }

    default ProductDescription mapDescription(String description) {
        return new ProductDescription(description);
    }

    default ProductPrice mapPrice(BigDecimal price, String currencyCode) {
        Currency currency = Currency.getInstance(currencyCode);
        return ProductPrice.of(price, currency);
    }

    default ProductRating mapRating(Double rating) {
        return ProductRating.of(rating);
    }

    default ProductCategory mapCategory(String category) {
        return new ProductCategory(category);
    }

    default ProductBrand mapBrand(String brand) {
        return new ProductBrand(brand);
    }
}