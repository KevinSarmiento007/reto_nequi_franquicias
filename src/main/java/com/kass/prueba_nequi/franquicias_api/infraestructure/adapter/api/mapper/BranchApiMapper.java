package com.kass.prueba_nequi.franquicias_api.infraestructure.adapter.api.mapper;

import com.kass.prueba_nequi.franquicias_api.domain.model.Branch;
import com.kass.prueba_nequi.franquicias_api.infraestructure.adapter.api.dto.BranchRequest;
import com.kass.prueba_nequi.franquicias_api.infraestructure.adapter.api.dto.BranchResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BranchApiMapper {
    Branch toDomain(BranchRequest request);
    BranchResponse toResponse(Branch branch);
}
