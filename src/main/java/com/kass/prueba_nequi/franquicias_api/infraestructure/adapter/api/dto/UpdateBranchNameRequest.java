package com.kass.prueba_nequi.franquicias_api.infraestructure.adapter.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateBranchNameRequest(
        @NotNull(message = "Branch ID cannot be null")
        Long branchId,
        @NotBlank(message = "Branch name cannot be blank")
        String name
) {
}
