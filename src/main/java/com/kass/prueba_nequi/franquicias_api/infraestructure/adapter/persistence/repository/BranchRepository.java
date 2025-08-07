package com.kass.prueba_nequi.franquicias_api.infraestructure.adapter.persistence.repository;

import com.kass.prueba_nequi.franquicias_api.infraestructure.adapter.persistence.entity.BranchEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BranchRepository extends ReactiveCrudRepository<BranchEntity, Long> {
    Mono<BranchEntity> findByName(String name);
    Flux<BranchEntity> findByFranchiseId(Long franchiseId);
}
