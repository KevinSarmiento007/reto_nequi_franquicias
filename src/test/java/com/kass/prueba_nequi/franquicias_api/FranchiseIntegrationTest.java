package com.kass.prueba_nequi.franquicias_api;

import com.kass.prueba_nequi.franquicias_api.domain.api.FranchiseServicePort;
import com.kass.prueba_nequi.franquicias_api.domain.model.Branch;
import com.kass.prueba_nequi.franquicias_api.domain.model.Franchise;
import com.kass.prueba_nequi.franquicias_api.domain.model.Product;
import com.kass.prueba_nequi.franquicias_api.infraestructure.adapter.api.dto.*;
import com.kass.prueba_nequi.franquicias_api.infraestructure.adapter.api.handler.FranchiseHandler;
import com.kass.prueba_nequi.franquicias_api.infraestructure.adapter.api.mapper.BranchApiMapper;
import com.kass.prueba_nequi.franquicias_api.infraestructure.adapter.api.mapper.FranchiseApiMapper;
import com.kass.prueba_nequi.franquicias_api.infraestructure.adapter.api.mapper.ProductApiMapper;
import com.kass.prueba_nequi.franquicias_api.infraestructure.adapter.api.router.FranchiseRouter;
import jakarta.validation.Validator;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.util.List;

// @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@WebFluxTest
@AutoConfigureWebTestClient
@ContextConfiguration(classes = { FranchiseRouter.class, FranchiseHandler.class })
public class FranchiseIntegrationTest {

    @Autowired
    private WebTestClient client;

    @MockitoBean
    private FranchiseServicePort franchiseServicePort;

    @MockitoBean
    private FranchiseApiMapper franchiseApiMapper;

    @MockitoBean
    private Validator validator;

    @MockitoBean
    private BranchApiMapper branchApiMapper;

    @MockitoBean
    private ProductApiMapper productApiMapper;

    @Test
    void shouldReturnFranchise() {
        client.get()
                .uri("/ping")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void createFranchiseShouldReturnCreatedFranchiseWhenRequestIsValid() {
        FranchiseRequest requestDto = new FranchiseRequest("Franquicia de Prueba");

        Franchise savedFranchise = new Franchise(1L, "Franquicia de Prueba", List.of());
        when(franchiseServicePort.createFranchise(any(Franchise.class))).thenReturn(Mono.just(savedFranchise));
        when(franchiseApiMapper.toDomain(any(FranchiseRequest.class))).thenReturn(savedFranchise);
        when(franchiseApiMapper.toResponse(any(Franchise.class))).thenReturn(new FranchiseResponse(1L, "Franquicia de Prueba"));

        client.post().uri("/franchises")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestDto)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(FranchiseResponse.class)
                .isEqualTo(new FranchiseResponse(1L, "Franquicia de Prueba"));
    }

    @Test
    void addBranchToFranchise_ShouldReturnCreatedBranch_WhenRequestIsValid() {
        BranchRequest requestDto = new BranchRequest(1L, "Sucursal 1");

        Branch savedBranch = new Branch(101L,  "Sucursal 1", 1L, List.of());
        when(franchiseServicePort.addBranchToFranchise(anyLong(), any(Branch.class))).thenReturn(Mono.just(savedBranch));
        when(branchApiMapper.toDomain(any(BranchRequest.class))).thenReturn(savedBranch);
        when(branchApiMapper.toResponse(any(Branch.class))).thenReturn(new BranchResponse(101L, 1L, "Sucursal 1"));

        client.post().uri("/franchises/branches")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestDto)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(BranchResponse.class)
                .isEqualTo(new BranchResponse(101L, 1L, "Sucursal 1"));
    }

    @Test
    void addProductToBranch_ShouldReturnCreatedProduct_WhenRequestIsValid() {
        Long branchId = 101L;
        ProductRequest requestDto = new ProductRequest("Producto A", 100, 101L);

        Product savedProduct = new Product(201L,  "Producto A", 100, 101L);
        when(franchiseServicePort.addProductToBranch(anyLong(), any(Product.class))).thenReturn(Mono.just(savedProduct));
        when(productApiMapper.toDomain(any(ProductRequest.class))).thenReturn(savedProduct);
        when(productApiMapper.toResponse(any(Product.class))).thenReturn(new ProductResponse(201L, "Producto A", 100, 101L));

        client.post().uri("/branches/products", branchId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestDto)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(ProductResponse.class)
                .isEqualTo(new ProductResponse(201L, "Producto A", 100, 101L));
    }

    @Test
    void deleteProductFromBranch_ShouldReturnNoContent_WhenProductExists() {
        Long branchId = 101L;
        Long productId = 201L;

        when(franchiseServicePort.deleteProductFromBranch(anyLong(), anyLong())).thenReturn(Mono.empty());
        client.delete().uri("/branches/{branchId}/products/{productId}", branchId, productId)
                .exchange()
                .expectStatus().isNoContent();
    }
}
