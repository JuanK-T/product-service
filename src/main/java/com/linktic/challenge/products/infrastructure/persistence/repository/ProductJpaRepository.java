package com.linktic.challenge.products.infrastructure.persistence.repository;

import com.linktic.challenge.products.infrastructure.persistence.entity.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductJpaRepository extends JpaRepository<ProductEntity, String> {

    // Buscar por ID
    Optional<ProductEntity> findById(String id);

    // Listar con paginación
    Page<ProductEntity> findAll(Pageable pageable);

    // Eliminar por ID (ya viene por defecto)
    void deleteById(String id);

    // Método personalizado para verificar existencia
    boolean existsById(String id);

    // Método para eliminar en lote (opcional, para mejor performance)
    @Modifying
    @Query("DELETE FROM ProductEntity p WHERE p.id = :id")
    void deleteProductById(@Param("id") String id);

}
