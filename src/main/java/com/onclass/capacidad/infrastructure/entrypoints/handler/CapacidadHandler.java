package com.onclass.capacidad.infrastructure.entrypoints.handler;

import com.onclass.capacidad.domain.api.ICapacidadServicePort;
import com.onclass.capacidad.domain.excepcion.CapacidadException;
import com.onclass.capacidad.infrastructure.entrypoints.dto.CapacidadPageResponse;
import com.onclass.capacidad.infrastructure.entrypoints.dto.CapacidadRequest;
import com.onclass.capacidad.infrastructure.entrypoints.dto.CapacidadResponse;
import com.onclass.capacidad.infrastructure.entrypoints.mapper.CapacidadMapper;
import com.onclass.capacidad.infrastructure.entrypoints.util.ErrorDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class CapacidadHandler {

    private final ICapacidadServicePort capacidadServicePort;
    private final CapacidadMapper capacidadMapper;

    public Mono<ServerResponse> registrar(ServerRequest request) {
        return request.bodyToMono(CapacidadRequest.class)
                .map(capacidadMapper::toDomain)
                .flatMap(capacidadServicePort::registrar)
                .map(capacidadMapper::toResponse)
                .flatMap(response -> ServerResponse
                        .status(HttpStatus.CREATED)
                        .bodyValue(response))
                .onErrorResume(CapacidadException.class, e -> {
                    log.error("Error de negocio: {}", e.getMessage());
                    return ServerResponse
                            .status(HttpStatus.BAD_REQUEST)
                            .bodyValue(ErrorDTO.builder()
                                    .code(e.getCode())
                                    .message(e.getMessage())
                                    .build());
                })
                .onErrorResume(Exception.class, e -> {
                    log.error("Error inesperado: {}", e.getMessage());
                    return ServerResponse
                            .status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .bodyValue(ErrorDTO.builder()
                                    .code("CAP-500")
                                    .message("Error interno del servidor")
                                    .build());
                });
    }

    public Mono<ServerResponse> listar(ServerRequest request) {
        return ServerResponse.ok()
                .body(capacidadServicePort.listarTodas()
                        .map(capacidadMapper::toResponse), CapacidadResponse.class);
    }


    public Mono<ServerResponse> listarPaginado(ServerRequest request) {
        int pagina = Integer.parseInt(request.queryParam("page").orElse("0"));
        int tamanio = Integer.parseInt(request.queryParam("size").orElse("10"));
        String ordenarPor = request.queryParam("ordenarPor").orElse("nombre");
        String direccion = request.queryParam("direccion").orElse("asc");

        return capacidadServicePort.listarPaginado(pagina, tamanio, ordenarPor, direccion)
                .map(page -> new CapacidadPageResponse(
                        page.getCapacidades().stream()
                                .map(capacidadMapper::toResponse)
                                .toList(),
                        page.getPaginaActual(),
                        page.getTotalPaginas(),
                        page.getTotalElementos()
                ))
                .flatMap(response -> ServerResponse.ok().bodyValue(response))
                .onErrorResume(CapacidadException.class, e ->
                        ServerResponse.badRequest().bodyValue(ErrorDTO.builder()
                                .code(e.getCode())
                                .message(e.getMessage())
                                .build()));
    }

    public Mono<ServerResponse> buscarPorId(ServerRequest request) {
        Long id = Long.valueOf(request.pathVariable("id"));
        return capacidadServicePort.buscarPorId(id)
                .map(capacidadMapper::toResponse)
                .flatMap(c -> ServerResponse.ok().bodyValue(c))
                .switchIfEmpty(ServerResponse.notFound().build());
    }
}