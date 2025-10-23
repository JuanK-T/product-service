package com.linktic.challenge.products.infrastructure.persistence.entity;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ProductSpecificationId implements Serializable {
    private String product;
    private String key;
}