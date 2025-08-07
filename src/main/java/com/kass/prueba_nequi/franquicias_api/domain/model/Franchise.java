package com.kass.prueba_nequi.franquicias_api.domain.model;

import java.util.List;

public record Franchise(Long id, String name, List<Branch> branches) {
}
