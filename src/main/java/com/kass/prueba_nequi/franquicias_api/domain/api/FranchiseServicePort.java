package com.kass.prueba_nequi.franquicias_api.domain.api;

import com.kass.prueba_nequi.franquicias_api.domain.model.Branch;
import com.kass.prueba_nequi.franquicias_api.domain.model.Franchise;
import com.kass.prueba_nequi.franquicias_api.domain.model.Product;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FranchiseServicePort {
    Mono<Franchise> createFranchise(Franchise franchise);
    Mono<Branch> addBranchToFranchise(Long franchiseId, Branch branch);
    Mono<Product> addProductToBranch(Long branchId, Product product);
    Mono<Void> deleteProductFromBranch(Long branchId, Long productId);
    Mono<Product> updateProductStock(Long branchId, Long productId, Integer newStock);
    Flux<Branch> getTopProductsPerBranch(Long franchiseId);
    Mono<Franchise> updateFranchiseName(Long franchiseId, String newName);
    Mono<Branch> updateBranchName(Long branchId, String newName);
    Mono<Product> updateProductName(Long productId, String newName);
}
