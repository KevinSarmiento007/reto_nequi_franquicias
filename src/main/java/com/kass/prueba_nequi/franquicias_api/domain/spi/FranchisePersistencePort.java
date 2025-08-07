package com.kass.prueba_nequi.franquicias_api.domain.spi;

import com.kass.prueba_nequi.franquicias_api.domain.model.Branch;
import com.kass.prueba_nequi.franquicias_api.domain.model.Franchise;
import com.kass.prueba_nequi.franquicias_api.domain.model.Product;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FranchisePersistencePort {
    Mono<Franchise> saveFranchise(Franchise franchise);
    Mono<Boolean> existsByName(String name);
    Mono<Franchise> findFranchiseById(Long id);
    Mono<Branch> saveBranch(Branch branch);
    Mono<Boolean> existsBranchByName(String name);
    Mono<Branch> findBranchById(Long id);
    Mono<Product> saveProduct(Product product);
    Mono<Boolean> existsProductInBranch(Long branchId, String name);
    Mono<Product> findProductById(Long productId);
    Mono<Void> deleteProduct(Long productId);
    Mono<Product> updateProductStock(Long productId, Integer newStock);
    Flux<Branch> findBranchesByFranchiseId(Long franchiseId);
    Mono<Product> findTopProductByBranchId(Long branchId);
    Mono<Franchise> updateFranchiseName(Long franchiseId, String newName);
}
