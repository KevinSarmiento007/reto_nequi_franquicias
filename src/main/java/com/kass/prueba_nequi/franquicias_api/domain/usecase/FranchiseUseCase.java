package com.kass.prueba_nequi.franquicias_api.domain.usecase;

import com.kass.prueba_nequi.franquicias_api.domain.api.FranchiseServicePort;
import com.kass.prueba_nequi.franquicias_api.domain.enums.TechnicalMessage;
import com.kass.prueba_nequi.franquicias_api.domain.exceptions.BusinessException;
import com.kass.prueba_nequi.franquicias_api.domain.model.Branch;
import com.kass.prueba_nequi.franquicias_api.domain.model.Franchise;
import com.kass.prueba_nequi.franquicias_api.domain.model.Product;
import com.kass.prueba_nequi.franquicias_api.domain.spi.FranchisePersistencePort;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public class FranchiseUseCase implements FranchiseServicePort {

    private final FranchisePersistencePort franchisePersistencePort;

    public FranchiseUseCase(FranchisePersistencePort franchisePersistencePort) {
        this.franchisePersistencePort = franchisePersistencePort;
    }

    @Override
    public Mono<Franchise> createFranchise(Franchise franchise) {
        return franchisePersistencePort.existsByName(franchise.name())
                .flatMap(exist -> {
                    if(exist) {
                        return Mono.error(new BusinessException(TechnicalMessage.FRANCHISE_ALREADY_EXISTS));
                    }
                    return franchisePersistencePort.saveFranchise(franchise);
                });
    }

    @Override
    public Mono<Branch> addBranchToFranchise(Long franchiseId, Branch branch) {
        return franchisePersistencePort.findFranchiseById(franchiseId)
                .switchIfEmpty(Mono.error(new BusinessException(TechnicalMessage.FRANCHISE_NOT_FOUND)))
                .flatMap(franchise ->
                        franchisePersistencePort.existsBranchByName(branch.name())
                                .flatMap(exists -> {
                                    if (exists) {
                                        return Mono.error(new BusinessException(TechnicalMessage.BRANCH_ALREADY_EXISTS));
                                    }
                                    return franchisePersistencePort.saveBranch(new Branch(null, branch.name(), franchiseId, null));
                                })
                );
    }

    @Override
    public Mono<Product> addProductToBranch(Long branchId, Product product) {
        return franchisePersistencePort.findBranchById(branchId)
                .switchIfEmpty(Mono.error(new BusinessException(TechnicalMessage.BRANCH_NOT_FOUND)))
                .flatMap(branch ->
                        franchisePersistencePort.existsProductInBranch(branchId, product.name())
                                .flatMap(exists -> {
                                    if(exists){
                                        return Mono.error(new BusinessException(TechnicalMessage.PRODUCT_ALREADY_EXISTS));
                                    }
                                    return franchisePersistencePort.saveProduct(product);
                                }));
    }

    @Override
    public Mono<Void> deleteProductFromBranch(Long branchId, Long productId) {
        return franchisePersistencePort.findBranchById(branchId)
                .switchIfEmpty(Mono.error(new BusinessException(TechnicalMessage.BRANCH_NOT_FOUND)))
                .flatMap(branch ->
                        franchisePersistencePort.findProductById(productId)
                                .switchIfEmpty(Mono.error(new BusinessException(TechnicalMessage.PRODUCT_NOT_FOUND)))
                                .flatMap(product -> {
                                    if(!product.branchId().equals(branchId)){
                                        return Mono.error(new BusinessException(TechnicalMessage.PRODUCT_NOT_BELONG));
                                    }
                                    return franchisePersistencePort.deleteProduct(productId);
                                }));
    }

    @Override
    public Mono<Product> updateProductStock(Long branchId, Long productId, Integer newStock) {
        return franchisePersistencePort.findBranchById(branchId)
                .switchIfEmpty(Mono.error(new BusinessException(TechnicalMessage.BRANCH_NOT_FOUND)))
                .flatMap(branch ->
                        franchisePersistencePort.findProductById(productId)
                                .switchIfEmpty(Mono.error(new BusinessException(TechnicalMessage.PRODUCT_NOT_FOUND)))
                                .flatMap(product -> {
                                    if(!product.branchId().equals(branchId)){
                                        return Mono.error(new BusinessException(TechnicalMessage.PRODUCT_NOT_BELONG));
                                    }
                                    return franchisePersistencePort.updateProductStock(productId, newStock);
                                }));
    }

    @Override
    public Flux<Branch> getTopProductsPerBranch(Long franchiseId) {
        return franchisePersistencePort.findFranchiseById(franchiseId)
                .switchIfEmpty(Mono.error(new BusinessException(TechnicalMessage.FRANCHISE_NOT_FOUND)))
                .flatMapMany(franchise -> franchisePersistencePort.findBranchesByFranchiseId(franchiseId)
                        .flatMap(branch ->
                                franchisePersistencePort.findTopProductByBranchId(branch.id())
                                        .map(product -> new Branch(branch.id(), branch.name(), branch.franchiseId(), List.of(product)))
                                        .defaultIfEmpty(new Branch(branch.id(), branch.name(), branch.franchiseId(), List.of()))));
    }

    @Override
    public Mono<Franchise> updateFranchiseName(Long franchiseId, String newName) {
        return franchisePersistencePort.existsByName(newName)
                .flatMap(exists -> {
                    if(exists){
                        return Mono.error(new BusinessException(TechnicalMessage.FRANCHISE_ALREADY_EXISTS));
                    }
                    return franchisePersistencePort.findFranchiseById(franchiseId)
                            .switchIfEmpty(Mono.error(new BusinessException(TechnicalMessage.FRANCHISE_NOT_FOUND)))
                            .flatMap(franchise -> franchisePersistencePort.updateFranchiseName(franchiseId, newName));
                });
    }
}
