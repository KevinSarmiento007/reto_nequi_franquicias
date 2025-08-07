package com.kass.prueba_nequi.franquicias_api.infraestructure.adapter.api.dto;

import java.util.List;

public record BranchWithTopProductResponse(Long id, String name, List<ProductResponse> products) {
}
