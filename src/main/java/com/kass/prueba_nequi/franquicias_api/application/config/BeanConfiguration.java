package com.kass.prueba_nequi.franquicias_api.application.config;

import com.kass.prueba_nequi.franquicias_api.domain.api.FranchiseServicePort;
import com.kass.prueba_nequi.franquicias_api.domain.spi.FranchisePersistencePort;
import com.kass.prueba_nequi.franquicias_api.domain.usecase.FranchiseUseCase;
import com.kass.prueba_nequi.franquicias_api.infraestructure.adapter.persistence.FranchisePersistenceAdapter;
import com.kass.prueba_nequi.franquicias_api.infraestructure.adapter.persistence.mapper.BranchEntityMapper;
import com.kass.prueba_nequi.franquicias_api.infraestructure.adapter.persistence.mapper.FranchiseEntityMapper;
import com.kass.prueba_nequi.franquicias_api.infraestructure.adapter.persistence.repository.BranchRepository;
import com.kass.prueba_nequi.franquicias_api.infraestructure.adapter.persistence.repository.FranchiseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class BeanConfiguration {

    private final FranchiseRepository franchiseRepository;
    private final BranchRepository branchRepository;
    private final FranchiseEntityMapper franchiseEntityMapper;
    private final BranchEntityMapper branchEntityMapper;

    @Bean
    public FranchisePersistencePort franchisePersistencePort() {
        return new FranchisePersistenceAdapter(franchiseRepository, franchiseEntityMapper, branchRepository, branchEntityMapper);
    }

    @Bean
    public FranchiseServicePort franchiseServicePort(FranchisePersistencePort franchisePersistencePort) {
        return new FranchiseUseCase(franchisePersistencePort);
    }
}
