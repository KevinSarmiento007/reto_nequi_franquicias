package com.kass.prueba_nequi.franquicias_api.domain.usecase;

import com.kass.prueba_nequi.franquicias_api.domain.api.FranchiseServicePort;
import com.kass.prueba_nequi.franquicias_api.domain.enums.TechnicalMessage;
import com.kass.prueba_nequi.franquicias_api.domain.exceptions.BusinessException;
import com.kass.prueba_nequi.franquicias_api.domain.model.Franchise;
import com.kass.prueba_nequi.franquicias_api.domain.spi.FranchisePersistencePort;
import reactor.core.publisher.Mono;

public class FranchiseUseCase implements FranchiseServicePort {

    private final FranchisePersistencePort franchisePersistencePort;

    public FranchiseUseCase(FranchisePersistencePort franchisePersistencePort) {
        this.franchisePersistencePort = franchisePersistencePort;
    }

    @Override
    public Mono<Franchise> createFranchise(Franchise franchise) {
        return franchisePersistencePort.existsByName(franchise.name())
                .flatMap(exist -> {
                    if(exist) {
                        return Mono.error(new BusinessException(TechnicalMessage.FRANCHISE_ALREADY_EXISTS));
                    }
                    return franchisePersistencePort.saveFranchise(franchise);
                });
    }
}
