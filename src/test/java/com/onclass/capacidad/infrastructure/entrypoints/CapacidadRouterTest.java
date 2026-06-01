package com.onclass.capacidad.infrastructure.entrypoints;

import com.onclass.capacidad.domain.api.ICapacidadServicePort;
import com.onclass.capacidad.domain.excepcion.CapacidadException;
import com.onclass.capacidad.domain.model.Capacidad;
import com.onclass.capacidad.domain.model.CapacidadPage;
import com.onclass.capacidad.domain.model.Tecnologia;
import com.onclass.capacidad.infrastructure.entrypoints.dto.CapacidadRequest;
import com.onclass.capacidad.infrastructure.entrypoints.dto.CapacidadResponse;
import com.onclass.capacidad.infrastructure.entrypoints.dto.TecnologiaIdRequest;
import com.onclass.capacidad.infrastructure.entrypoints.dto.TecnologiaResponse;
import com.onclass.capacidad.infrastructure.entrypoints.handler.CapacidadHandler;
import com.onclass.capacidad.infrastructure.entrypoints.mapper.CapacidadMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@WebFluxTest
@Import({CapacidadRouter.class, CapacidadHandler.class})
class CapacidadRouterTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private ICapacidadServicePort servicePort;

    @MockBean
    private CapacidadMapper capacidadMapper;


    private CapacidadRequest requestValido() {
        return new CapacidadRequest(
                "Backend Developer",
                "Capacidad de desarrollo backend",
                List.of(
                        new TecnologiaIdRequest(1L),
                        new TecnologiaIdRequest(2L),
                        new TecnologiaIdRequest(3L)
                )
        );
    }

    private Capacidad capacidadDomain() {
        return new Capacidad(1L, "Backend Developer", "Capacidad de desarrollo backend",
                List.of(
                        new Tecnologia(1L, "Java"),
                        new Tecnologia(2L, "Spring"),
                        new Tecnologia(3L, "MySQL")
                ));
    }

    private CapacidadResponse capacidadResponse() {
        return new CapacidadResponse(1L, "Backend Developer", "Capacidad de desarrollo backend",
                List.of(
                        new TecnologiaResponse(1L, "Java"),
                        new TecnologiaResponse(2L, "Spring"),
                        new TecnologiaResponse(3L, "MySQL")
                ));
    }


    @Test
    void registrar_exitoso_retorna201() {
        Capacidad domain = capacidadDomain();
        CapacidadResponse response = capacidadResponse();

        when(capacidadMapper.toDomain(any(CapacidadRequest.class))).thenReturn(domain);
        when(servicePort.registrar(any())).thenReturn(Mono.just(domain));
        when(capacidadMapper.toResponse(any(Capacidad.class))).thenReturn(response);

        webTestClient.post()
                .uri("/api/v1/capacidades")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestValido())
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.id").isEqualTo(1)
                .jsonPath("$.nombre").isEqualTo("Backend Developer");
    }

    @Test
    void registrar_nombreDuplicado_retorna400() {
        when(capacidadMapper.toDomain(any(CapacidadRequest.class))).thenReturn(capacidadDomain());
        when(servicePort.registrar(any())).thenReturn(
                Mono.error(new CapacidadException("CAP-001", "Nombre duplicado")));

        webTestClient.post()
                .uri("/api/v1/capacidades")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestValido())
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.code").isEqualTo("CAP-001");
    }

    @Test
    void registrar_tecnologiaNoExiste_retorna400() {
        when(capacidadMapper.toDomain(any(CapacidadRequest.class))).thenReturn(capacidadDomain());
        when(servicePort.registrar(any())).thenReturn(
                Mono.error(new CapacidadException("CAP-005", "La tecnología no existe")));

        webTestClient.post()
                .uri("/api/v1/capacidades")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestValido())
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.code").isEqualTo("CAP-005");
    }

    @Test
    void registrar_errorInterno_retorna500() {
        when(capacidadMapper.toDomain(any(CapacidadRequest.class))).thenReturn(capacidadDomain());
        when(servicePort.registrar(any())).thenReturn(
                Mono.error(new RuntimeException("Error de BD")));

        webTestClient.post()
                .uri("/api/v1/capacidades")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestValido())
                .exchange()
                .expectStatus().is5xxServerError()
                .expectBody()
                .jsonPath("$.code").isEqualTo("CAP-500");
    }


    @Test
    void listar_retorna200ConLista() {
        when(servicePort.listarTodas()).thenReturn(Flux.just(capacidadDomain()));
        when(capacidadMapper.toResponse(any(Capacidad.class))).thenReturn(capacidadResponse());

        webTestClient.get()
                .uri("/api/v1/capacidades")
                .exchange()
                .expectStatus().isOk();
    }


    @Test
    void listarPaginado_retorna200() {
        CapacidadPage page = new CapacidadPage(List.of(capacidadDomain()), 0, 1, 1L);

        when(servicePort.listarPaginado(0, 10, "nombre", "asc"))
                .thenReturn(Mono.just(page));
        when(capacidadMapper.toResponse(any(Capacidad.class))).thenReturn(capacidadResponse());

        webTestClient.get()
                .uri("/api/v1/capacidades/paginado?page=0&size=10&ordenarPor=nombre&direccion=asc")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.paginaActual").isEqualTo(0)
                .jsonPath("$.totalElementos").isEqualTo(1);
    }

    @Test
    void listarPaginado_ordenadoPorCantidadTecnologias_retorna200() {
        CapacidadPage page = new CapacidadPage(List.of(capacidadDomain()), 0, 1, 1L);

        when(servicePort.listarPaginado(0, 5, "cantidadTecnologias", "desc"))
                .thenReturn(Mono.just(page));
        when(capacidadMapper.toResponse(any(Capacidad.class))).thenReturn(capacidadResponse());

        webTestClient.get()
                .uri("/api/v1/capacidades/paginado?page=0&size=5&ordenarPor=cantidadTecnologias&direccion=desc")
                .exchange()
                .expectStatus().isOk();
    }


    @Test
    void buscarPorId_existe_retorna200() {
        when(servicePort.buscarPorId(1L)).thenReturn(Mono.just(capacidadDomain()));
        when(capacidadMapper.toResponse(any(Capacidad.class))).thenReturn(capacidadResponse());

        webTestClient.get()
                .uri("/api/v1/capacidades/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(1);
    }

    @Test
    void buscarPorId_noExiste_retorna404() {
        when(servicePort.buscarPorId(anyLong())).thenReturn(Mono.empty());

        webTestClient.get()
                .uri("/api/v1/capacidades/999")
                .exchange()
                .expectStatus().isNotFound();
    }



    @Test
    void eliminar_exitoso_retorna204() {
        when(servicePort.eliminar(1L)).thenReturn(Mono.empty());

        webTestClient.delete()
                .uri("/api/v1/capacidades/1")
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void eliminar_noExiste_retorna404() {
        when(servicePort.eliminar(anyLong())).thenReturn(
                Mono.error(new CapacidadException("CAP-010", "La capacidad no existe")));

        webTestClient.delete()
                .uri("/api/v1/capacidades/999")
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.code").isEqualTo("CAP-010");
    }
}