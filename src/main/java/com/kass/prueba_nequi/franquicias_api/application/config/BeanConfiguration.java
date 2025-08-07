package com.kass.prueba_nequi.franquicias_api.application.config;

import com.kass.prueba_nequi.franquicias_api.domain.api.FranchiseServicePort;
import com.kass.prueba_nequi.franquicias_api.domain.spi.FranchisePersistencePort;
import com.kass.prueba_nequi.franquicias_api.domain.usecase.FranchiseUseCase;
import com.kass.prueba_nequi.franquicias_api.infraestructure.adapter.persistence.FranchisePersistenceAdapter;
import com.kass.prueba_nequi.franquicias_api.infraestructure.adapter.persistence.mapper.FranchiseEntityMapper;
import com.kass.prueba_nequi.franquicias_api.infraestructure.adapter.persistence.repository.FranchiseRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    @Bean
    public FranchisePersistencePort franchisePersistencePort(FranchiseRepository franchiseRepository,
                                                             FranchiseEntityMapper franchiseEntityMapper){
        return new FranchisePersistenceAdapter(franchiseRepository, franchiseEntityMapper);
    }

    @Bean
    public FranchiseServicePort franchiseServicePort(FranchisePersistencePort franchisePersistencePort){
        return new FranchiseUseCase(franchisePersistencePort);
    }
}
