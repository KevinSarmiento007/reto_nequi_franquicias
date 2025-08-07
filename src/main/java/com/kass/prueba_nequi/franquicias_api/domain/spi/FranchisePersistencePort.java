package com.kass.prueba_nequi.franquicias_api.domain.spi;

import com.kass.prueba_nequi.franquicias_api.domain.model.Branch;
import com.kass.prueba_nequi.franquicias_api.domain.model.Franchise;
import reactor.core.publisher.Mono;

public interface FranchisePersistencePort {
    Mono<Franchise> saveFranchise(Franchise franchise);
    Mono<Boolean> existsByName(String name);
    Mono<Franchise> findFranchiseById(Long id);
    Mono<Branch> saveBranch(Branch branch);
    Mono<Boolean> existsBranchByName(String name);
}
