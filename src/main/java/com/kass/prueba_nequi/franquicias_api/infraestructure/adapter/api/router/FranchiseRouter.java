package com.kass.prueba_nequi.franquicias_api.infraestructure.adapter.api.router;

import com.kass.prueba_nequi.franquicias_api.infraestructure.adapter.api.dto.*;
import com.kass.prueba_nequi.franquicias_api.infraestructure.adapter.api.handler.FranchiseHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import static org.springframework.web.reactive.function.server.RouterFunctions.*;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class FranchiseRouter {

    @RouterOperations({
            @RouterOperation(
                    path = "/franchises",
                    produces = MediaType.APPLICATION_JSON_VALUE,
                    method = RequestMethod.POST,
                    beanClass = FranchiseHandler.class,
                    beanMethod = "createFranchise",
                    operation = @Operation(
                            operationId = "createFranchise",
                            tags = "Franchises",
                            summary = "Create a new franchise",
                            requestBody = @RequestBody(
                                    description = "Franchise creation request",
                                    required = true,
                                    content = @Content(schema = @Schema(implementation = FranchiseRequest.class))
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "201",
                                            description = "Franchise created successfully",
                                            content = @Content(schema = @Schema(implementation = FranchiseResponse.class))
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = "/franchises/branches",
                    produces = MediaType.APPLICATION_JSON_VALUE,
                    method = RequestMethod.POST,
                    beanClass = FranchiseHandler.class,
                    beanMethod = "addBranchToFranchise",
                    operation = @Operation(
                            operationId = "addBranchToFranchise",
                            tags = "Branches",
                            summary = "Add a new branch to an existing franchise",
                            parameters = {
                                    @Parameter(name = "franchiseId", in = ParameterIn.PATH, required = true, description = "ID of the franchise")
                            },
                            requestBody = @RequestBody(
                                    description = "Branch creation request",
                                    required = true,
                                    content = @Content(schema = @Schema(implementation = BranchRequest.class))
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "201",
                                            description = "Branch added successfully",
                                            content = @Content(schema = @Schema(implementation = BranchResponse.class))
                                    ),
                                    @ApiResponse(responseCode = "404", description = "Franchise not found")
                            }
                    )
            ),
            @RouterOperation(
                    path = "/branches/products",
                    produces = MediaType.APPLICATION_JSON_VALUE,
                    method = RequestMethod.POST,
                    beanClass = FranchiseHandler.class,
                    beanMethod = "addProductToBranch",
                    operation = @Operation(
                            operationId = "addProductToBranch",
                            tags = "Products",
                            summary = "Add a new product to a specific branch",
                            parameters = {
                                    @Parameter(name = "branchId", in = ParameterIn.PATH, required = true, description = "ID of the branch")
                            },
                            requestBody = @RequestBody(
                                    description = "Product creation request",
                                    required = true,
                                    content = @Content(schema = @Schema(implementation = ProductRequest.class))
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "201",
                                            description = "Product added successfully",
                                            content = @Content(schema = @Schema(implementation = ProductResponse.class))
                                    ),
                                    @ApiResponse(responseCode = "404", description = "Branch not found")
                            }
                    )
            ),
            @RouterOperation(
                    path = "/branches/{branchId}/products/{productId}",
                    method = RequestMethod.DELETE,
                    beanClass = FranchiseHandler.class,
                    beanMethod = "deleteProductFromBranch",
                    operation = @Operation(
                            operationId = "deleteProductFromBranch",
                            tags = {"Products"},
                            summary = "Delete a product from a specific branch",
                            parameters = {
                                    @Parameter(name = "branchId", in = ParameterIn.PATH, required = true, description = "ID of the branch"),
                                    @Parameter(name = "productId", in = ParameterIn.PATH, required = true, description = "ID of the product to delete")
                            },
                            responses = {
                                    @ApiResponse(responseCode = "204", description = "Product deleted successfully"),
                                    @ApiResponse(responseCode = "404", description = "Branch or product not found"),
                                    @ApiResponse(responseCode = "500", description = "Internal server error")
                            }
                    )
            ),
            @RouterOperation(
                    path = "/branches/{branchId}/products/{productId}",
                    produces = MediaType.APPLICATION_JSON_VALUE,
                    method = RequestMethod.PUT,
                    beanClass = FranchiseHandler.class,
                    beanMethod = "updateProductStock",
                    operation = @Operation(
                            operationId = "updateProductStock",
                            tags = {"Products"},
                            summary = "Update the stock of a product",
                            parameters = {
                                    @Parameter(name = "branchId", in = ParameterIn.PATH, required = true, description = "ID of the branch"),
                                    @Parameter(name = "productId", in = ParameterIn.PATH, required = true, description = "ID of the product")
                            },
                            requestBody = @RequestBody(
                                    content = @Content(schema = @Schema(implementation = UpdateProductStockRequest.class))
                            ),
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "Product stock updated successfully", content = @Content(schema = @Schema(implementation = ProductResponse.class))),
                                    @ApiResponse(responseCode = "400", description = "Invalid request or stock value"),
                                    @ApiResponse(responseCode = "404", description = "Branch or product not found"),
                                    @ApiResponse(responseCode = "500", description = "Internal server error")
                            }
                    )
            ),
            @RouterOperation(
                    path = "/franchises/{franchiseId}/top-products",
                    produces = MediaType.APPLICATION_JSON_VALUE,
                    method = RequestMethod.GET,
                    beanClass = FranchiseHandler.class,
                    beanMethod = "getTopProductsPerBranch",
                    operation = @Operation(
                            operationId = "getTopProductsPerBranch",
                            tags = {"Franchises"},
                            summary = "Get the product with the highest stock for each branch of a franchise",
                            parameters = {
                                    @Parameter(name = "franchiseId", in = ParameterIn.PATH, required = true, description = "ID of the franchise")
                            },
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "Successful retrieval", content = @Content(schema = @Schema(implementation = BranchWithTopProductResponse.class))),
                                    @ApiResponse(responseCode = "404", description = "Franchise not found"),
                                    @ApiResponse(responseCode = "500", description = "Internal server error")
                            }
                    )
            ),
            @RouterOperation(
                    path = "/franchises/{franchiseId}",
                    produces = MediaType.APPLICATION_JSON_VALUE,
                    method = RequestMethod.PUT,
                    beanClass = FranchiseHandler.class,
                    beanMethod = "updateFranchiseName",
                    operation = @Operation(
                            operationId = "updateFranchiseName",
                            tags = {"Franchises"},
                            summary = "Update the name of a franchise",
                            parameters = {
                                    @Parameter(name = "franchiseId", in = ParameterIn.PATH, required = true, description = "ID of the franchise")
                            },
                            requestBody = @RequestBody(
                                    content = @Content(schema = @Schema(implementation = UpdatedFranchiseNameRequest.class))
                            ),
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "Franchise name updated successfully", content = @Content(schema = @Schema(implementation = FranchiseResponse.class))),
                                    @ApiResponse(responseCode = "400", description = "Invalid request or name already exists"),
                                    @ApiResponse(responseCode = "404", description = "Franchise not found"),
                                    @ApiResponse(responseCode = "500", description = "Internal server error")
                            }
                    )
            ),
            @RouterOperation(
                    path = "/branches/{branchId}",
                    produces = MediaType.APPLICATION_JSON_VALUE,
                    method = RequestMethod.PUT,
                    beanClass = FranchiseHandler.class,
                    beanMethod = "updateBranchName",
                    operation = @Operation(
                            operationId = "updateBranchName",
                            tags = {"Branches"},
                            summary = "Update the name of a branch",
                            parameters = {
                                    @Parameter(name = "branchId", in = ParameterIn.PATH, required = true, description = "ID of the branch")
                            },
                            requestBody = @RequestBody(
                                    content = @Content(schema = @Schema(implementation = UpdateBranchNameRequest.class))
                            ),
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "Branch name updated successfully", content = @Content(schema = @Schema(implementation = BranchResponse.class))),
                                    @ApiResponse(responseCode = "400", description = "Invalid request or name already exists"),
                                    @ApiResponse(responseCode = "404", description = "Branch not found"),
                                    @ApiResponse(responseCode = "500", description = "Internal server error")
                            }
                    )
            ),
            @RouterOperation(
                    path = "/products/{productId}",
                    produces = MediaType.APPLICATION_JSON_VALUE,
                    method = RequestMethod.PUT,
                    beanClass = FranchiseHandler.class,
                    beanMethod = "updateProductName",
                    operation = @Operation(
                            operationId = "updateProductName",
                            tags = {"Products"},
                            summary = "Update the name of a product",
                            parameters = {
                                    @Parameter(name = "productId", in = ParameterIn.PATH, required = true, description = "ID of the product")
                            },
                            requestBody = @RequestBody(
                                    content = @Content(schema = @Schema(implementation = UpdateProductNameRequest.class))
                            ),
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "Product name updated successfully", content = @Content(schema = @Schema(implementation = ProductResponse.class))),
                                    @ApiResponse(responseCode = "400", description = "Invalid request or name already exists"),
                                    @ApiResponse(responseCode = "404", description = "Product not found"),
                                    @ApiResponse(responseCode = "500", description = "Internal server error")
                            }
                    )
            ),
            @RouterOperation(
                    path = "/ping",
                    produces = MediaType.TEXT_PLAIN_VALUE,
                    method = RequestMethod.GET,
                    beanClass = FranchiseHandler.class,
                    beanMethod = "test",
                    operation = @Operation(
                            operationId = "ping",
                            summary = "Health check endpoint",
                            description = "Returns pong if the service is alive",
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Service is alive",
                                            content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE)
                                    )
                            }
                    )
            )
    })
    @Bean
    public RouterFunction<ServerResponse> franchiseRoutes(FranchiseHandler handler){
        return route(POST("/franchises").and(accept(MediaType.APPLICATION_JSON)), handler::createFranchise)
                .andRoute(POST("/franchises/branches").and(accept(MediaType.APPLICATION_JSON)),
                        handler::addBranchToFranchise)
                .andRoute(POST("/branches/products").and(accept(MediaType.APPLICATION_JSON)),
                        handler::addProductToBranch)
                .andRoute(DELETE("/branches/{branchId}/products/{productId}"), handler::deleteProductFromBranch)
                .andRoute(PUT("/branches/products"), handler::updateProductStock)
                .andRoute(GET("/franchises/{franchiseId}/top-products"), handler::getTopProductsPerBranch)
                .andRoute(PUT("/franchises"), handler::updateFranchiseName)
                .andRoute(PUT("/branches"), handler::updateBranchName)
                .andRoute(PUT("/products"), handler::updateProductName)
                .andRoute(GET("/ping"), handler::test);
    }
}
