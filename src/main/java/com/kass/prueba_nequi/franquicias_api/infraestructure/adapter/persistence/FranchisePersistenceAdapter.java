package com.kass.prueba_nequi.franquicias_api.infraestructure.adapter.persistence;

import com.kass.prueba_nequi.franquicias_api.domain.model.Branch;
import com.kass.prueba_nequi.franquicias_api.domain.model.Franchise;
import com.kass.prueba_nequi.franquicias_api.domain.model.Product;
import com.kass.prueba_nequi.franquicias_api.domain.spi.FranchisePersistencePort;
import com.kass.prueba_nequi.franquicias_api.infraestructure.adapter.persistence.entity.BranchEntity;
import com.kass.prueba_nequi.franquicias_api.infraestructure.adapter.persistence.entity.FranchiseEntity;
import com.kass.prueba_nequi.franquicias_api.infraestructure.adapter.persistence.entity.ProductEntity;
import com.kass.prueba_nequi.franquicias_api.infraestructure.adapter.persistence.mapper.BranchEntityMapper;
import com.kass.prueba_nequi.franquicias_api.infraestructure.adapter.persistence.mapper.FranchiseEntityMapper;
import com.kass.prueba_nequi.franquicias_api.infraestructure.adapter.persistence.mapper.ProductEntityMapper;
import com.kass.prueba_nequi.franquicias_api.infraestructure.adapter.persistence.repository.BranchRepository;
import com.kass.prueba_nequi.franquicias_api.infraestructure.adapter.persistence.repository.FranchiseRepository;
import com.kass.prueba_nequi.franquicias_api.infraestructure.adapter.persistence.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class FranchisePersistenceAdapter implements FranchisePersistencePort {

    private final FranchiseRepository franchiseRepository;
    private final FranchiseEntityMapper franchiseEntityMapper;
    private final BranchRepository branchRepository;
    private final BranchEntityMapper branchEntityMapper;
    private final ProductRepository productRepository;
    private final ProductEntityMapper productEntityMapper;

    @Override
    public Mono<Franchise> saveFranchise(Franchise franchise) {
        FranchiseEntity entity = franchiseEntityMapper.toEntity(franchise);
        return franchiseRepository.save(entity).map(franchiseEntityMapper::toModel);
    }

    @Override
    public Mono<Boolean> existsByName(String name) {
        return franchiseRepository.findByName(name).hasElement();
    }

    @Override
    public Mono<Franchise> findFranchiseById(Long id) {
        return franchiseRepository.findById(id).map(franchiseEntityMapper::toModel);
    }

    @Override
    public Mono<Branch> saveBranch(Branch branch) {
        BranchEntity entity = branchEntityMapper.toEntity(branch);
        return branchRepository.save(entity).map(branchEntityMapper::toModel);
    }

    @Override
    public Mono<Boolean> existsBranchByName(String name) {
        return branchRepository.findByName(name).hasElement();
    }

    @Override
    public Mono<Branch> findBranchById(Long id) {
        return branchRepository.findById(id).map(branchEntityMapper::toModel);
    }

    @Override
    public Mono<Product> saveProduct(Product product) {
        ProductEntity entity = productEntityMapper.toEntity(product);
        return productRepository.save(entity).map(productEntityMapper::toModel);
    }

    @Override
    public Mono<Boolean> existsProductInBranch(Long branchId, String name) {
        return productRepository.findByBranchIdAndName(branchId, name).hasElement();
    }

    @Override
    public Mono<Product> findProductById(Long productId) {
        return productRepository.findById(productId).map(productEntityMapper::toModel);
    }

    @Override
    public Mono<Void> deleteProduct(Long productId) {
        return productRepository.deleteById(productId);
    }

    @Override
    public Mono<Product> updateProductStock(Long productId, Integer newStock) {
        return productRepository.updateStock(productId, newStock)
                .flatMap(rowsUpdated ->{
                    if(rowsUpdated > 0){
                        return productRepository.findById(productId);
                    }
                    return Mono.empty();
                })
                .map(productEntityMapper::toModel);
    }

    @Override
    public Flux<Branch> findBranchesByFranchiseId(Long franchiseId) {
        return branchRepository.findByFranchiseId(franchiseId)
                .map(branchEntityMapper::toModel);
    }

    @Override
    public Mono<Product> findTopProductByBranchId(Long branchId) {
        return productRepository.findTopProductByBranchId(branchId)
                .map(productEntityMapper::toModel);
    }

    @Override
    public Mono<Franchise> updateFranchiseName(Long franchiseId, String newName) {
        return franchiseRepository.updateName(newName, franchiseId)
                .flatMap(updatedRows -> {
                    if(updatedRows > 0){
                        return franchiseRepository.findById(franchiseId);
                    }
                    return Mono.empty();
                })
                .map(franchiseEntityMapper::toModel);
    }

    @Override
    public Mono<Branch> updateBranchName(Long branchId, String newName) {
        return branchRepository.updateName(newName, branchId)
                .flatMap(updatedRows -> {
                    if(updatedRows > 0){
                        return branchRepository.findById(branchId);
                    }
                    return Mono.empty();
                })
                .map(branchEntityMapper::toModel);
    }

    @Override
    public Mono<Product> updateProductName(Long productId, String newName) {
        return productRepository.updateName(productId, newName)
                .flatMap(updatedRows -> {
                    if(updatedRows > 0){
                        return productRepository.findById(productId);
                    }
                    return Mono.empty();
                })
                .map(productEntityMapper::toModel);
    }
}
