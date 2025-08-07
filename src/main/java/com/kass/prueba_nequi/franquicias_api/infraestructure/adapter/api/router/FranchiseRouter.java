package com.kass.prueba_nequi.franquicias_api.infraestructure.adapter.api.router;

import com.kass.prueba_nequi.franquicias_api.infraestructure.adapter.api.handler.FranchiseHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;

import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import static org.springframework.web.reactive.function.server.RouterFunctions.*;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class FranchiseRouter {

    @Bean
    public RouterFunction<ServerResponse> franchiseRoutes(FranchiseHandler handler){
        return route(POST("/franchises").and(accept(MediaType.APPLICATION_JSON)), handler::createFranchise)
                .andRoute(POST("/franchises/branches").and(accept(MediaType.APPLICATION_JSON)),
                        handler::addBranchToFranchise)
                .andRoute(POST("/branches/products").and(accept(MediaType.APPLICATION_JSON)),
                        handler::addProductToBranch)
                .andRoute(DELETE("/branches/{branchId}/products/{productId}"), handler::deleteProductFromBranch);
    }
}
