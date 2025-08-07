package com.kass.prueba_nequi.franquicias_api.infraestructure.adapter.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateProductNameRequest(
        @NotNull(message = "Product ID cannot be null")
        Long productId,
        @NotBlank(message = "Product name cannot be null")
        String name
) {
}
