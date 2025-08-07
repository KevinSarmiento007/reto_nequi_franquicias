package com.kass.prueba_nequi.franquicias_api.infraestructure.adapter.persistence;

import com.kass.prueba_nequi.franquicias_api.domain.model.Franchise;
import com.kass.prueba_nequi.franquicias_api.domain.spi.FranchisePersistencePort;
import com.kass.prueba_nequi.franquicias_api.infraestructure.adapter.persistence.entity.FranchiseEntity;
import com.kass.prueba_nequi.franquicias_api.infraestructure.adapter.persistence.mapper.FranchiseEntityMapper;
import com.kass.prueba_nequi.franquicias_api.infraestructure.adapter.persistence.repository.FranchiseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class FranchisePersistenceAdapter implements FranchisePersistencePort {

    private final FranchiseRepository franchiseRepository;
    private final FranchiseEntityMapper franchiseEntityMapper;

    @Override
    public Mono<Franchise> saveFranchise(Franchise franchise) {
        FranchiseEntity entity = franchiseEntityMapper.toEntity(franchise);
        return franchiseRepository.save(entity).map(franchiseEntityMapper::toModel);
    }

    @Override
    public Mono<Boolean> existsByName(String name) {
        return franchiseRepository.findByName(name).hasElement();
    }
}
