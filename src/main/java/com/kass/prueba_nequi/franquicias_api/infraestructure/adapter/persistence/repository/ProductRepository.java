package com.kass.prueba_nequi.franquicias_api.infraestructure.adapter.persistence.repository;

import com.kass.prueba_nequi.franquicias_api.infraestructure.adapter.persistence.entity.ProductEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface ProductRepository extends ReactiveCrudRepository<ProductEntity, Long> {
    Mono<ProductEntity> findByBranchIdAndName(Long branchId, String name);
}
