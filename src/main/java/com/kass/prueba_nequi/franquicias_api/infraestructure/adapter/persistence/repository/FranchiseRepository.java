    package com.kass.prueba_nequi.franquicias_api.infraestructure.adapter.persistence.repository;

    import com.kass.prueba_nequi.franquicias_api.infraestructure.adapter.persistence.entity.FranchiseEntity;
    import org.springframework.data.r2dbc.repository.Modifying;
    import org.springframework.data.r2dbc.repository.Query;
    import org.springframework.data.repository.query.Param;
    import org.springframework.data.repository.reactive.ReactiveCrudRepository;
    import reactor.core.publisher.Mono;

    public interface FranchiseRepository extends ReactiveCrudRepository<FranchiseEntity, Long> {
        Mono<FranchiseEntity> findByName(String name);

        @Modifying
        @Query("UPDATE franquicias SET nombre = :newName WHERE id = :franchiseId")
        Mono<Integer> updateName(@Param("newName") String newName, @Param("franchiseId") Long franchiseId);
    }
