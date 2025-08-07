package com.kass.prueba_nequi.franquicias_api.infraestructure.adapter.api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record UpdateProductStockRequest(
        @NotNull(message = "branchId cannot be null")
        Long branchId,
        @NotNull(message = "productId cannot be null")
        Long productId,
        @NotNull(message = "Stock cannot be null")
        @Min(value = 0, message = "Stock must be a positive number")
        Integer stock
) {
}
