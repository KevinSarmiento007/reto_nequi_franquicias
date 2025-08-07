    package com.kass.prueba_nequi.franquicias_api.infraestructure.adapter.persistence.repository;

    import com.kass.prueba_nequi.franquicias_api.infraestructure.adapter.persistence.entity.FranchiseEntity;
    import org.springframework.data.repository.reactive.ReactiveCrudRepository;
    import reactor.core.publisher.Mono;

    public interface FranchiseRepository extends ReactiveCrudRepository<FranchiseEntity, Long> {
        Mono<FranchiseEntity> findByName(String name);
    }
