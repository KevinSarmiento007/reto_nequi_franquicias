package com.kass.prueba_nequi.franquicias_api.infraestructure.adapter.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdatedFranchiseNameRequest(
        @NotBlank(message = "Franchise name cannot be blank")
        String name,
        @NotNull(message = "Franchise ID cannot be null")
        Long franchiseId
) {
}
