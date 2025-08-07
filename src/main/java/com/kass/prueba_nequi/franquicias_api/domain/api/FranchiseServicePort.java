package com.kass.prueba_nequi.franquicias_api.domain.api;

import com.kass.prueba_nequi.franquicias_api.domain.model.Franchise;
import reactor.core.publisher.Mono;

public interface FranchiseServicePort {
    Mono<Franchise> createFranchise(Franchise franchise);
}
