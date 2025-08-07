package com.kass.prueba_nequi.franquicias_api.infraestructure.adapter.persistence;

import com.kass.prueba_nequi.franquicias_api.domain.model.Branch;
import com.kass.prueba_nequi.franquicias_api.domain.model.Franchise;
import com.kass.prueba_nequi.franquicias_api.domain.spi.FranchisePersistencePort;
import com.kass.prueba_nequi.franquicias_api.infraestructure.adapter.persistence.entity.BranchEntity;
import com.kass.prueba_nequi.franquicias_api.infraestructure.adapter.persistence.entity.FranchiseEntity;
import com.kass.prueba_nequi.franquicias_api.infraestructure.adapter.persistence.mapper.BranchEntityMapper;
import com.kass.prueba_nequi.franquicias_api.infraestructure.adapter.persistence.mapper.FranchiseEntityMapper;
import com.kass.prueba_nequi.franquicias_api.infraestructure.adapter.persistence.repository.BranchRepository;
import com.kass.prueba_nequi.franquicias_api.infraestructure.adapter.persistence.repository.FranchiseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class FranchisePersistenceAdapter implements FranchisePersistencePort {

    private final FranchiseRepository franchiseRepository;
    private final FranchiseEntityMapper franchiseEntityMapper;
    private final BranchRepository branchRepository;
    private final BranchEntityMapper branchEntityMapper;

    @Override
    public Mono<Franchise> saveFranchise(Franchise franchise) {
        FranchiseEntity entity = franchiseEntityMapper.toEntity(franchise);
        return franchiseRepository.save(entity).map(franchiseEntityMapper::toModel);
    }

    @Override
    public Mono<Boolean> existsByName(String name) {
        return franchiseRepository.findByName(name).hasElement();
    }

    @Override
    public Mono<Franchise> findFranchiseById(Long id) {
        return franchiseRepository.findById(id).map(franchiseEntityMapper::toModel);
    }

    @Override
    public Mono<Branch> saveBranch(Branch branch) {
        BranchEntity entity = branchEntityMapper.toEntity(branch);
        return branchRepository.save(entity).map(branchEntityMapper::toModel);
    }

    @Override
    public Mono<Boolean> existsBranchByName(String name) {
        return branchRepository.findByName(name).hasElement();
    }
}
