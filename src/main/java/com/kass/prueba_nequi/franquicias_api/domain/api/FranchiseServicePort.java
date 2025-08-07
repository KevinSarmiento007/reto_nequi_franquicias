package com.kass.prueba_nequi.franquicias_api.domain.api;

import com.kass.prueba_nequi.franquicias_api.domain.model.Branch;
import com.kass.prueba_nequi.franquicias_api.domain.model.Franchise;
import com.kass.prueba_nequi.franquicias_api.domain.model.Product;
import reactor.core.publisher.Mono;

public interface FranchiseServicePort {
    Mono<Franchise> createFranchise(Franchise franchise);
    Mono<Branch> addBranchToFranchise(Long franchiseId, Branch branch);
    Mono<Product> addProductToBranch(Long branchId, Product product);
}
