package com.kass.prueba_nequi.franquicias_api.infraestructure.adapter.api.handler;

import com.kass.prueba_nequi.franquicias_api.domain.api.FranchiseServicePort;
import com.kass.prueba_nequi.franquicias_api.infraestructure.adapter.api.dto.*;
import com.kass.prueba_nequi.franquicias_api.infraestructure.adapter.api.mapper.BranchApiMapper;
import com.kass.prueba_nequi.franquicias_api.infraestructure.adapter.api.mapper.FranchiseApiMapper;
import com.kass.prueba_nequi.franquicias_api.infraestructure.adapter.api.mapper.ProductApiMapper;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class FranchiseHandler {
    private final FranchiseServicePort franchiseServicePort;
    private final FranchiseApiMapper franchiseApiMapper;
    private final Validator validator;
    private final BranchApiMapper branchApiMapper;
    private final ProductApiMapper productApiMapper;

    public Mono<ServerResponse> createFranchise(ServerRequest request){
        return request.bodyToMono(FranchiseRequest.class)
                .flatMap(franchiseRequest -> {
                    var violations = validator.validate(franchiseRequest);
                    if (!violations.isEmpty()) {
                        String errorMessage = violations.iterator().next().getMessage();
                        return ServerResponse.badRequest().bodyValue(errorMessage);
                    }
                    return Mono.just(franchiseRequest)
                            .map(franchiseApiMapper::toDomain)
                            .flatMap(franchiseServicePort::createFranchise)
                            .map(franchiseApiMapper::toResponse)
                            .flatMap(response -> ServerResponse.status(HttpStatus.CREATED).bodyValue(response));
                });

    }

    public Mono<ServerResponse> addBranchToFranchise(ServerRequest request){
        return request.bodyToMono(BranchRequest.class)
                .flatMap(branchRequest -> {
                    var violations = validator.validate(branchRequest);
                    if (!violations.isEmpty()) {
                        String errorMessage = violations.iterator().next().getMessage();
                        return ServerResponse.badRequest().bodyValue(errorMessage);
                    }
                    return Mono.just(branchRequest)
                            .map(branchApiMapper::toDomain)
                            .flatMap(branch -> franchiseServicePort.addBranchToFranchise(branch.franchiseId(), branch))
                            .map(branchApiMapper::toResponse)
                            .flatMap(response -> ServerResponse.status(HttpStatus.CREATED).bodyValue(response));
                });
    }

    public Mono<ServerResponse> addProductToBranch(ServerRequest request){
        return request.bodyToMono(ProductRequest.class)
                .flatMap(productRequest -> {
                    var violations = validator.validate(productRequest);
                    if (!violations.isEmpty()) {
                        String errorMessage = violations.iterator().next().getMessage();
                        return ServerResponse.badRequest().bodyValue(errorMessage);
                    }
                    return Mono.just(productRequest)
                            .map(productApiMapper::toDomain)
                            .flatMap(product -> franchiseServicePort.addProductToBranch(product.branchId(), product))
                            .map(productApiMapper::toResponse)
                            .flatMap(response -> ServerResponse.status(HttpStatus.CREATED).bodyValue(response));
                });
    }

    public Mono<ServerResponse> deleteProductFromBranch(ServerRequest request){
        Long branchId = Long.valueOf(request.pathVariable("branchId"));
        Long productId = Long.valueOf(request.pathVariable("productId"));
        return franchiseServicePort.deleteProductFromBranch(branchId, productId)
                .then(ServerResponse.noContent().build());
    }

    public Mono<ServerResponse> updateProductStock(ServerRequest request){
        return request.bodyToMono(UpdateProductStockRequest.class)
                .flatMap(updateProductStockRequest -> {
                    var violations = validator.validate(updateProductStockRequest);
                    if (!violations.isEmpty()) {
                        String errorMessage = violations.iterator().next().getMessage();
                        return ServerResponse.badRequest().bodyValue(errorMessage);
                    }
                    return Mono.just(updateProductStockRequest)
                            .flatMap(updateRequest ->
                                    franchiseServicePort.updateProductStock(updateRequest.branchId(), updateRequest.productId(), updateRequest.stock()))
                            .map(productApiMapper::toResponse)
                            .flatMap(response -> ServerResponse.ok().bodyValue(response));
                });
    }

    public Mono<ServerResponse> getTopProductsPerBranch(ServerRequest request){
        Long franchiseId = Long.valueOf(request.pathVariable("franchiseId"));
        return franchiseServicePort.getTopProductsPerBranch(franchiseId)
                .map(branch -> new BranchWithTopProductResponse(branch.id(), branch.name(), branch.products()
                        .stream().map(productApiMapper::toResponse).toList()))
                .collectList()
                .flatMap(response -> ServerResponse.ok().bodyValue(response));
    }

    public Mono<ServerResponse> updateFranchiseName(ServerRequest request){
        return request.bodyToMono(UpdatedFranchiseNameRequest.class)
                .flatMap(updatedFranchiseNameRequest -> {
                    var violations = validator.validate(updatedFranchiseNameRequest);
                    if (!violations.isEmpty()) {
                        String errorMessage = violations.iterator().next().getMessage();
                        return ServerResponse.badRequest().bodyValue(errorMessage);
                    }
                    return Mono.just(updatedFranchiseNameRequest)
                            .flatMap(req -> franchiseServicePort.updateFranchiseName(req.franchiseId(), req.name()))
                            .map(franchiseApiMapper::toResponse)
                            .flatMap(response -> ServerResponse.ok().bodyValue(response));
                });
    }

    public Mono<ServerResponse> updateBranchName(ServerRequest request){
        return request.bodyToMono(UpdateBranchNameRequest.class)
                .flatMap(body -> {
                    var violations = validator.validate(body);
                    if (!violations.isEmpty()) {
                        String errorMessage = violations.iterator().next().getMessage();
                        return ServerResponse.badRequest().bodyValue(errorMessage);
                    }
                    return Mono.just(body)
                            .flatMap(newBody -> franchiseServicePort.updateBranchName(newBody.branchId(), newBody.name()))
                            .map(branchApiMapper::toResponse)
                            .flatMap(response -> ServerResponse.ok().bodyValue(response));
                });
    }
}
