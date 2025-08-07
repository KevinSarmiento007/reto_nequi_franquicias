package com.kass.prueba_nequi.franquicias_api.infraestructure.adapter.persistence.mapper;

import com.kass.prueba_nequi.franquicias_api.domain.model.Franchise;
import com.kass.prueba_nequi.franquicias_api.infraestructure.adapter.persistence.entity.FranchiseEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FranchiseEntityMapper {
    @Mapping(target = "branches", ignore = true)
    Franchise toModel(FranchiseEntity franchiseEntity);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    FranchiseEntity toEntity(Franchise franchise);
}
