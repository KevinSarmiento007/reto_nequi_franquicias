package com.kass.prueba_nequi.franquicias_api.infraestructure.adapter.persistence.repository;

import com.kass.prueba_nequi.franquicias_api.infraestructure.adapter.persistence.entity.BranchEntity;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BranchRepository extends ReactiveCrudRepository<BranchEntity, Long> {
    Mono<BranchEntity> findByName(String name);
    Flux<BranchEntity> findByFranchiseId(Long franchiseId);

    @Modifying
    @Query("UPDATE sucursales SET nombre = :newName WHERE id = :branchId")
    Mono<Integer> updateName(@Param("newName") String newName, @Param("branchId") Long id);
}
