package com.kass.prueba_nequi.franquicias_api.infraestructure.adapter.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record BranchRequest(
        @NotNull(message = "Franchise ID cannot be null")
        Long franchiseId,
        @NotBlank(message = "Branch name cannot be blank")
        String name
) {
}
