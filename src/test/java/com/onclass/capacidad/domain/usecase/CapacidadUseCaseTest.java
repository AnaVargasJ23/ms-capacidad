package com.onclass.capacidad.domain.usecase;

import com.onclass.capacidad.domain.excepcion.CapacidadException;
import com.onclass.capacidad.domain.model.Capacidad;
import com.onclass.capacidad.domain.model.Tecnologia;
import com.onclass.capacidad.domain.spi.ICapacidadPersistencePort;
import com.onclass.capacidad.domain.spi.ITecnologiaServicePort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CapacidadUseCaseTest {

    @Mock
    private ICapacidadPersistencePort persistencePort;

    @Mock
    private ITecnologiaServicePort tecnologiaServicePort;

    @InjectMocks
    private CapacidadUseCase useCase;

    private List<Tecnologia> tecnologiasValidas() {
        return List.of(
                new Tecnologia(1L, "Java"),
                new Tecnologia(2L, "Spring Boot"),
                new Tecnologia(3L, "MySQL")
        );
    }

    @Test
    void registrar_exitoso() {
        Capacidad capacidad = new Capacidad(null, "Backend", "Descripción válida", tecnologiasValidas());
        when(tecnologiaServicePort.existeTecnologia(any())).thenReturn(Mono.just(true));
        when(persistencePort.existePorNombre("Backend")).thenReturn(Mono.just(false));
        when(persistencePort.guardar(any())).thenReturn(Mono.just(new Capacidad(1L, "Backend", "Descripción válida", tecnologiasValidas())));

        StepVerifier.create(useCase.registrar(capacidad))
                .expectNextMatches(c -> c.getId() == 1L && c.getNombre().equals("Backend"))
                .verifyComplete();
    }

    @Test
    void registrar_nombreVacio_lanzaError() {
        Capacidad capacidad = new Capacidad(null, "", "Descripción válida", tecnologiasValidas());

        StepVerifier.create(useCase.registrar(capacidad))
                .expectError(CapacidadException.class)
                .verify();
    }

    @Test
    void registrar_nombreNull_lanzaError() {
        Capacidad capacidad = new Capacidad(null, null, "Descripción válida", tecnologiasValidas());

        StepVerifier.create(useCase.registrar(capacidad))
                .expectError(CapacidadException.class)
                .verify();
    }

    @Test
    void registrar_nombreMayorA50Chars_lanzaError() {
        Capacidad capacidad = new Capacidad(null, "A".repeat(51), "Descripción válida", tecnologiasValidas());

        StepVerifier.create(useCase.registrar(capacidad))
                .expectError(CapacidadException.class)
                .verify();
    }

    @Test
    void registrar_descripcionVacia_lanzaError() {
        Capacidad capacidad = new Capacidad(null, "Backend", "", tecnologiasValidas());

        StepVerifier.create(useCase.registrar(capacidad))
                .expectError(CapacidadException.class)
                .verify();
    }

    @Test
    void registrar_descripcionNull_lanzaError() {
        Capacidad capacidad = new Capacidad(null, "Backend", null, tecnologiasValidas());

        StepVerifier.create(useCase.registrar(capacidad))
                .expectError(CapacidadException.class)
                .verify();
    }

    @Test
    void registrar_descripcionMayorA90Chars_lanzaError() {
        Capacidad capacidad = new Capacidad(null, "Backend", "A".repeat(91), tecnologiasValidas());

        StepVerifier.create(useCase.registrar(capacidad))
                .expectError(CapacidadException.class)
                .verify();
    }

    @Test
    void registrar_menosDe3Tecnologias_lanzaError() {
        Capacidad capacidad = new Capacidad(null, "Backend", "Descripción válida",
                List.of(new Tecnologia(1L, "Java"), new Tecnologia(2L, "Spring")));

        StepVerifier.create(useCase.registrar(capacidad))
                .expectError(CapacidadException.class)
                .verify();
    }

    @Test
    void registrar_masDe20Tecnologias_lanzaError() {
        List<Tecnologia> tecnologias = java.util.stream.LongStream.rangeClosed(1, 21)
                .mapToObj(i -> new Tecnologia(i, "Tecnologia" + i))
                .toList();
        Capacidad capacidad = new Capacidad(null, "Backend", "Descripción válida", tecnologias);

        StepVerifier.create(useCase.registrar(capacidad))
                .expectError(CapacidadException.class)
                .verify();
    }

    @Test
    void registrar_tecnologiasRepetidas_lanzaError() {
        Capacidad capacidad = new Capacidad(null, "Backend", "Descripción válida",
                List.of(new Tecnologia(1L, "Java"), new Tecnologia(1L, "Java"), new Tecnologia(2L, "Spring")));

        StepVerifier.create(useCase.registrar(capacidad))
                .expectError(CapacidadException.class)
                .verify();
    }

    @Test
    void registrar_tecnologiaNoExiste_lanzaError() {
        List<Tecnologia> tecnologias = List.of(
                new Tecnologia(1L, "Java"),
                new Tecnologia(2L, "Spring Boot"),
                new Tecnologia(3L, "MySQL")
        );
        Capacidad capacidad = new Capacidad(null, "Backend", "Descripción válida", tecnologias);

        when(tecnologiaServicePort.existeTecnologia(anyLong())).thenReturn(Mono.just(false));

        StepVerifier.create(useCase.registrar(capacidad))
                .expectError(CapacidadException.class)
                .verify();
    }

    @Test
    void registrar_nombreDuplicado_lanzaError() {
        Capacidad capacidad = new Capacidad(null, "Backend", "Descripción válida", tecnologiasValidas());
        when(tecnologiaServicePort.existeTecnologia(any())).thenReturn(Mono.just(true));
        when(persistencePort.existePorNombre("Backend")).thenReturn(Mono.just(true));

        StepVerifier.create(useCase.registrar(capacidad))
                .expectError(CapacidadException.class)
                .verify();
    }

    @Test
    void listarTodas_retornaLista() {
        when(persistencePort.listarTodas()).thenReturn(Flux.just(
                new Capacidad(1L, "Backend", "Descripción", tecnologiasValidas()),
                new Capacidad(2L, "Frontend", "Descripción", tecnologiasValidas())
        ));

        StepVerifier.create(useCase.listarTodas())
                .expectNextCount(2)
                .verifyComplete();
    }
}