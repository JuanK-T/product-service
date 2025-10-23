package com.linktic.challenge.products.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "product_specifications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(ProductSpecificationId.class)
public class ProductSpecificationEntity {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private ProductEntity product;

    @Id
    @Column(name = "spec_key", nullable = false, length = 50)
    private String key;

    @Column(name = "spec_value", nullable = false, length = 80)
    private String value;
}