package com.kass.prueba_nequi.franquicias_api.infraestructure.adapter.api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProductRequest(
        @NotBlank(message = "Product name cannot be blank")
        String name,
        @NotNull(message = "Stock cannot be null")
        @Min(value = 0, message = "Stock must be a positive number")
        int stock,
        @NotNull(message = "Branch ID cannot be null")
        Long branchId
) {
}
