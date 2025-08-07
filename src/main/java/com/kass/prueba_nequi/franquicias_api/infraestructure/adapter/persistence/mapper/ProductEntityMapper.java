package com.kass.prueba_nequi.franquicias_api.infraestructure.adapter.persistence.mapper;

import com.kass.prueba_nequi.franquicias_api.domain.model.Product;
import com.kass.prueba_nequi.franquicias_api.infraestructure.adapter.persistence.entity.ProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductEntityMapper {
    Product toModel(ProductEntity entity);

    @Mapping(target = "name", source = "name")
    @Mapping(target = "stock", source = "stock")
    @Mapping(target = "branchId", source = "branchId")
    ProductEntity toEntity(Product product);
}
