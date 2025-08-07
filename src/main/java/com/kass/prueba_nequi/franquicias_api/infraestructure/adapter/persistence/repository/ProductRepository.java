package com.kass.prueba_nequi.franquicias_api.infraestructure.adapter.persistence.repository;

import com.kass.prueba_nequi.franquicias_api.infraestructure.adapter.persistence.entity.ProductEntity;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface ProductRepository extends ReactiveCrudRepository<ProductEntity, Long> {
    Mono<ProductEntity> findByBranchIdAndName(Long branchId, String name);

    @Modifying
    @Query("UPDATE productos SET stock = :newStock WHERE id = :productId")
    Mono<Integer> updateStock(@Param("productId") Long productId, @Param("newStock") Integer newStock);

    @Query("SELECT * FROM productos p WHERE p.sucursal_id = :branchId ORDER BY p.stock DESC LIMIT 1")
    Mono<ProductEntity> findTopProductByBranchId(@Param("branchId") Long branchId);

    @Modifying
    @Query("UPDATE productos SET nombre = :newName WHERE id = :productId")
    Mono<Integer> updateName(@Param("productId") Long productId, @Param("newName") String newName);
}
