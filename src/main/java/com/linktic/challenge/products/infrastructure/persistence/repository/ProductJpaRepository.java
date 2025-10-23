package com.linktic.challenge.products.infrastructure.persistence.repository;

import com.linktic.challenge.products.infrastructure.persistence.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductJpaRepository extends JpaRepository<ProductEntity, String> {

    // ✅ Método para verificar si existe un producto con el mismo nombre
    boolean existsByName(String name);

    // ✅ Método para verificar si existe un producto con el mismo nombre pero diferente ID
    boolean existsByNameAndIdNot(String name, String id);

    // Método para eliminar en lote (opcional, para mejor performance)
    @Modifying
    @Query("DELETE FROM ProductEntity p WHERE p.id = :id")
    void deleteProductById(@Param("id") String id);

}
