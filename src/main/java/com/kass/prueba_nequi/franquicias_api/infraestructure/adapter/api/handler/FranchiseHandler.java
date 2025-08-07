package com.kass.prueba_nequi.franquicias_api.infraestructure.adapter.api.handler;

import com.kass.prueba_nequi.franquicias_api.domain.api.FranchiseServicePort;
import com.kass.prueba_nequi.franquicias_api.infraestructure.adapter.api.dto.BranchRequest;
import com.kass.prueba_nequi.franquicias_api.infraestructure.adapter.api.dto.FranchiseRequest;
import com.kass.prueba_nequi.franquicias_api.infraestructure.adapter.api.mapper.BranchApiMapper;
import com.kass.prueba_nequi.franquicias_api.infraestructure.adapter.api.mapper.FranchiseApiMapper;
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
}
