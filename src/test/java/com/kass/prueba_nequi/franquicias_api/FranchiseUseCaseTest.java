package com.kass.prueba_nequi.franquicias_api;

import com.kass.prueba_nequi.franquicias_api.domain.enums.TechnicalMessage;
import com.kass.prueba_nequi.franquicias_api.domain.exceptions.BusinessException;
import com.kass.prueba_nequi.franquicias_api.domain.model.Branch;
import com.kass.prueba_nequi.franquicias_api.domain.model.Franchise;
import com.kass.prueba_nequi.franquicias_api.domain.model.Product;
import com.kass.prueba_nequi.franquicias_api.domain.spi.FranchisePersistencePort;
import com.kass.prueba_nequi.franquicias_api.domain.usecase.FranchiseUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

import java.util.List;

public class FranchiseUseCaseTest {

    private FranchiseUseCase franchiseUseCase;
    private FranchisePersistencePort franchisePersistencePort;

    @BeforeEach
    void setUp() {
        franchisePersistencePort = Mockito.mock(FranchisePersistencePort.class);
        franchiseUseCase = new FranchiseUseCase(franchisePersistencePort);
    }

    @Test
    void createFranchiseShouldReturnFranchiseWhenNameIsUnique() {
        Franchise franchiseTest = new Franchise(null, "Franchise Test", List.of());
        when(franchisePersistencePort.existsByName(anyString())).thenReturn(Mono.just(false));
        when(franchisePersistencePort.saveFranchise(any(Franchise.class)))
                .thenReturn(Mono.just(new Franchise(1L, "Franchise Test", List.of())));

        StepVerifier.create(franchiseUseCase.createFranchise(franchiseTest))
                .expectNextMatches(f -> f.id() != null && f.name().equals("Franchise Test"))
                .verifyComplete();
    }

    @Test
    void createFranchiseShouldReturnThrowExceptionWhenNameAlreadyExists() {
        Franchise franquicia = new Franchise(null, "Franquicia", List.of());
        when(franchisePersistencePort.existsByName("Franquicia")).thenReturn(Mono.just(true));
        StepVerifier.create(franchiseUseCase.createFranchise(franquicia))
                .expectError(BusinessException.class)
                .verify();
    }

    @Test
    void addBanchToFranchiseShouldReturnBranchWhenFranchiseExistsAndNameIsUnique() {
        Branch branchTest = new Branch(null, "branch test", 1L, List.of());
        when(franchisePersistencePort.findFranchiseById(1L)).thenReturn(Mono.just(new Franchise(1L, "Franquicia", List.of())));
        when(franchisePersistencePort.existsBranchByName(anyString())).thenReturn(Mono.just(false));
        when(franchisePersistencePort.saveBranch(any(Branch.class))).thenReturn(Mono.just(new Branch(1L, "branch test", 1L, List.of())));

        StepVerifier.create(franchiseUseCase.addBranchToFranchise(1L, branchTest))
                .expectNextMatches(b -> b.id() == 1L && b.name().equals("branch test"))
                .verifyComplete();
    }

    @Test
    void addBranchToFranchiseShouldThrowExceptionWhenFranchiseNotFound() {
        Branch sucursalInexistente = new Branch(null, "Sucursal Inexistente", 99L, List.of());
        when(franchisePersistencePort.findFranchiseById(99L)).thenReturn(Mono.empty());
        StepVerifier.create(franchiseUseCase.addBranchToFranchise(99L, sucursalInexistente))
                .expectErrorMatches(e -> e instanceof BusinessException && e.getMessage().contains(TechnicalMessage.FRANCHISE_NOT_FOUND.getMessage()))
                .verify();
    }

    @Test
    void addBranchToFranchiseShouldThrowExceptionWhenBranchNameAlreadyExists() {
        Branch newBranch = new Branch(null,  "Sucursal Existente", 1L, List.of());
        when(franchisePersistencePort.findFranchiseById(1L)).thenReturn(Mono.just(new Franchise(1L, "Franquicia", List.of())));
        when(franchisePersistencePort.existsBranchByName(anyString())).thenReturn(Mono.just(true));

        StepVerifier.create(franchiseUseCase.addBranchToFranchise(1L, newBranch))
                .expectErrorMatches(e -> e instanceof BusinessException && e.getMessage().contains(TechnicalMessage.BRANCH_ALREADY_EXISTS.getMessage()))
                .verify();
    }

    @Test
    void addProductToBranch_ShouldReturnProduct_WhenBranchExistsAndNameIsUnique() {
        Product newProduct = new Product(null,  "Producto Test", 50,101L);
        when(franchisePersistencePort.findBranchById(101L)).thenReturn(Mono.just(new Branch(101L,  "Sucursal", 1L, List.of())));
        when(franchisePersistencePort.existsProductInBranch(anyLong(), anyString())).thenReturn(Mono.just(false));
        when(franchisePersistencePort.saveProduct(any(Product.class))).thenReturn(Mono.just(new Product(201L,  "Producto Test", 50, 101L)));

        StepVerifier.create(franchiseUseCase.addProductToBranch(101L, newProduct))
                .expectNextMatches(p -> p.id() != null && p.name().equals("Producto Test"))
                .verifyComplete();
    }

}
