package com.kass.prueba_nequi.franquicias_api.infraestructure.adapter.persistence.mapper;

import com.kass.prueba_nequi.franquicias_api.domain.model.Branch;
import com.kass.prueba_nequi.franquicias_api.infraestructure.adapter.persistence.entity.BranchEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BranchEntityMapper {
    @Mapping(target = "products", ignore = true)
    Branch toModel(BranchEntity entity);

    @Mapping(target = "name", source = "name")
    @Mapping(target = "franchiseId", source = "franchiseId")
    BranchEntity toEntity(Branch branch);
}
