package com.onclass.capacidad.infrastructure.entrypoints;

import com.onclass.capacidad.infrastructure.entrypoints.dto.CapacidadRequest;
import com.onclass.capacidad.infrastructure.entrypoints.handler.CapacidadHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
@Tag(name = "Capacidad", description = "Gestión de capacidades del sistema On-Class")
public class CapacidadRouter {

    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/api/v1/capacidades",
                    method = RequestMethod.POST,
                    beanClass = CapacidadHandler.class,
                    beanMethod = "registrar",
                    operation = @Operation(
                            operationId = "registrarCapacidad",
                            summary = "Registrar una nueva capacidad",
                            tags = {"Capacidad"},
                            requestBody = @RequestBody(
                                    required = true,
                                    content = @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = CapacidadRequest.class)
                                    )
                            ),
                            responses = {
                                    @ApiResponse(responseCode = "201", description = "Capacidad creada exitosamente"),
                                    @ApiResponse(responseCode = "400", description = "Datos inválidos o reglas de negocio violadas")
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/v1/capacidades",
                    method = RequestMethod.GET,
                    beanClass = CapacidadHandler.class,
                    beanMethod = "listar",
                    operation = @Operation(
                            operationId = "listarCapacidades",
                            summary = "Listar todas las capacidades",
                            tags = {"Capacidad"},
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "Lista de capacidades")
                            }
                    )
            )
    })
    public RouterFunction<ServerResponse> capacidadRoutes(CapacidadHandler handler) {
        return RouterFunctions.route()
                .POST("/api/v1/capacidades", handler::registrar)
                .GET("/api/v1/capacidades", handler::listar)
                .build();
    }
}