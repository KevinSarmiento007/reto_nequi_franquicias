package com.kass.prueba_nequi.franquicias_api.domain.spi;

import com.kass.prueba_nequi.franquicias_api.domain.model.Franchise;
import reactor.core.publisher.Mono;

public interface FranchisePersistencePort {
    Mono<Franchise> saveFranchise(Franchise franchise);
    Mono<Boolean> existsByName(String name);
}
