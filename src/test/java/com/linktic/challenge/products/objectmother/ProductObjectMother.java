package com.linktic.challenge.products.objectmother;

import com.linktic.challenge.products.domain.model.*;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Map;

public class ProductObjectMother {

    public static Product smartphoneGalaxyXZ() {
        Map<String, String> specs = Map.of(
                "pantalla", "6.5 pulgadas AMOLED",
                "procesador", "Snapdragon 8 Gen 2",
                "memoria", "256GB",
                "camara", "108MP + 12MP + 5MP",
                "bateria", "4800mAh"
        );

        return new Product(
                new ProductId("prod001"),
                new ProductName("Smartphone Galaxy XZ"),
                new ProductImageUrl("https://example.com/galaxy-xz.jpg"),
                new ProductDescription("Smartphone flagship con cámara profesional"),
                new ProductPrice(new BigDecimal("899.99"), Currency.getInstance("USD")),
                new ProductRating(4.7),
                new ProductCategory("Electrónica"),
                new ProductBrand("TechNova"),
                new ProductSpecifications(specs)
        );
    }

    public static Product smartphoneWithDifferentPrice(BigDecimal price) {
        Product base = smartphoneGalaxyXZ();
        return new Product(
                base.id(),
                base.name(),
                base.imageUrl(),
                base.description(),
                new ProductPrice(price, Currency.getInstance("USD")),
                base.rating(),
                base.category(),
                base.brand(),
                base.specifications()
        );
    }

    public static Product smartphoneWithDifferentRating(double rating) {
        Product base = smartphoneGalaxyXZ();
        return new Product(
                base.id(),
                base.name(),
                base.imageUrl(),
                base.description(),
                base.price(),
                new ProductRating(rating),
                base.category(),
                base.brand(),
                base.specifications()
        );
    }

    public static Product laptopPro() {
        Map<String, String> specs = Map.of(
                "pantalla", "14 pulgadas Retina",
                "procesador", "Intel Core i7",
                "memoria", "16GB RAM",
                "almacenamiento", "512GB SSD",
                "sistema_operativo", "Windows 11"
        );

        return new Product(
                new ProductId("prod002"),
                new ProductName("Laptop Pro Ultra"),
                new ProductImageUrl("https://example.com/laptop-pro.jpg"),
                new ProductDescription("Laptop profesional para trabajo intensivo"),
                new ProductPrice(new BigDecimal("1299.99"), Currency.getInstance("USD")),
                new ProductRating(4.5),
                new ProductCategory("Computadoras"),
                new ProductBrand("TechMaster"),
                new ProductSpecifications(specs)
        );
    }
}