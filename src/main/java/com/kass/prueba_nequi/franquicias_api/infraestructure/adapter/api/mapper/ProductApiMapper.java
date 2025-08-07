package com.kass.prueba_nequi.franquicias_api.infraestructure.adapter.api.mapper;

import com.kass.prueba_nequi.franquicias_api.domain.model.Product;
import com.kass.prueba_nequi.franquicias_api.infraestructure.adapter.api.dto.ProductRequest;
import com.kass.prueba_nequi.franquicias_api.infraestructure.adapter.api.dto.ProductResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductApiMapper {
    Product toDomain(ProductRequest request);
    ProductResponse toResponse(Product product);
}
