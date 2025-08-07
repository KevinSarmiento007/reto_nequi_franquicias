package com.kass.prueba_nequi.franquicias_api.domain.model;

import java.util.List;

public record Branch(Long id, String name, Long franchiseId, List<Product> products) {
}
