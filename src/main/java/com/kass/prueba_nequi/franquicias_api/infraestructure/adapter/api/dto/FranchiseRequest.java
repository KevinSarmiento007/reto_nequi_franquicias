package com.kass.prueba_nequi.franquicias_api.infraestructure.adapter.api.dto;

import jakarta.validation.constraints.NotBlank;

public record FranchiseRequest(@NotBlank(message = "Franchise name cannot be blank") String name) {
}
