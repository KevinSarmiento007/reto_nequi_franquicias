package com.kass.prueba_nequi.franquicias_api.infraestructure.adapter.api.mapper;

import com.kass.prueba_nequi.franquicias_api.domain.model.Franchise;
import com.kass.prueba_nequi.franquicias_api.infraestructure.adapter.api.dto.FranchiseRequest;
import com.kass.prueba_nequi.franquicias_api.infraestructure.adapter.api.dto.FranchiseResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FranchiseApiMapper {
    Franchise toDomain(FranchiseRequest request);
    FranchiseResponse toResponse(Franchise franchise);
}
