package com.onclass.capacidad.domain.usecase;

import com.onclass.capacidad.domain.api.ICapacidadServicePort;
import com.onclass.capacidad.domain.constants.CapacidadConstants;
import com.onclass.capacidad.domain.enums.CapacidadErrorEnum;
import com.onclass.capacidad.domain.excepcion.CapacidadException;
import com.onclass.capacidad.domain.model.Capacidad;
import com.onclass.capacidad.domain.model.CapacidadPage;
import com.onclass.capacidad.domain.model.Tecnologia;
import com.onclass.capacidad.domain.spi.ICapacidadPersistencePort;
import com.onclass.capacidad.domain.spi.ITecnologiaServicePort;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
public class CapacidadUseCase implements ICapacidadServicePort {

    private final ICapacidadPersistencePort persistencePort;
    private final ITecnologiaServicePort tecnologiaServicePort;

    @Override
    public Mono<Capacidad> registrar(Capacidad capacidad) {
        try {
            validar(capacidad);
        } catch (CapacidadException e) {
            return Mono.error(e);
        }
        return validarTecnologiasExisten(capacidad.getTecnologias())
                .flatMap(ignored -> persistencePort.existePorNombre(capacidad.getNombre()))
                .flatMap(existe -> {
                    if (existe) {
                        return Mono.error(new CapacidadException(
                                CapacidadErrorEnum.NOMBRE_DUPLICADO.getCode(),
                                CapacidadErrorEnum.NOMBRE_DUPLICADO.getMessage()));
                    }
                    return persistencePort.guardar(capacidad);
                });
    }

    @Override
    public Flux<Capacidad> listarTodas() {
        return persistencePort.listarTodas();
    }

    private void validar(Capacidad capacidad) {
        if (capacidad.getNombre() == null || capacidad.getNombre().isBlank()) {
            throw new CapacidadException(
                    CapacidadErrorEnum.NOMBRE_OBLIGATORIO.getCode(),
                    CapacidadErrorEnum.NOMBRE_OBLIGATORIO.getMessage());
        }
        if (capacidad.getNombre().length() > CapacidadConstants.NOMBRE_MAX_LENGTH) {
            throw new CapacidadException(
                    CapacidadErrorEnum.NOMBRE_MAX_50.getCode(),
                    CapacidadErrorEnum.NOMBRE_MAX_50.getMessage());
        }
        if (capacidad.getDescripcion() == null || capacidad.getDescripcion().isBlank()) {
            throw new CapacidadException(
                    CapacidadErrorEnum.DESCRIPCION_OBLIGATORIA.getCode(),
                    CapacidadErrorEnum.DESCRIPCION_OBLIGATORIA.getMessage());
        }
        if (capacidad.getDescripcion().length() > CapacidadConstants.DESCRIPCION_MAX_LENGTH) {
            throw new CapacidadException(
                    CapacidadErrorEnum.DESCRIPCION_MAX_90.getCode(),
                    CapacidadErrorEnum.DESCRIPCION_MAX_90.getMessage());
        }
        if (capacidad.getTecnologias() == null
                || capacidad.getTecnologias().size() < CapacidadConstants.TECNOLOGIAS_MIN) {
            throw new CapacidadException(
                    CapacidadErrorEnum.TECNOLOGIAS_MIN_3.getCode(),
                    CapacidadErrorEnum.TECNOLOGIAS_MIN_3.getMessage());
        }
        if (capacidad.getTecnologias().size() > CapacidadConstants.TECNOLOGIAS_MAX) {
            throw new CapacidadException(
                    CapacidadErrorEnum.TECNOLOGIAS_MAX_20.getCode(),
                    CapacidadErrorEnum.TECNOLOGIAS_MAX_20.getMessage());
        }
        List<Long> ids = capacidad.getTecnologias().stream()
                .map(Tecnologia::getId).toList();
        Set<Long> idsUnicos = new HashSet<>(ids);
        if (idsUnicos.size() != ids.size()) {
            throw new CapacidadException(
                    CapacidadErrorEnum.TECNOLOGIAS_REPETIDAS.getCode(),
                    CapacidadErrorEnum.TECNOLOGIAS_REPETIDAS.getMessage());
        }
    }

    private Mono<Boolean> validarTecnologiasExisten(List<Tecnologia> tecnologias) {
        return Flux.fromIterable(tecnologias)
                .flatMap(t -> tecnologiaServicePort.existeTecnologia(t.getId())
                        .flatMap(existe -> {
                            if (!existe) {
                                return Mono.error(new CapacidadException(
                                        CapacidadErrorEnum.TECNOLOGIA_NO_EXISTE.getCode(),
                                        CapacidadErrorEnum.TECNOLOGIA_NO_EXISTE.getMessage()));
                            }
                            return Mono.just(existe);
                        }))
                .all(Boolean::booleanValue);
    }

    @Override
    public Mono<CapacidadPage> listarPaginado(int pagina, int tamanio, String ordenarPor, String direccion) {
        String ordenValido = (ordenarPor == null || ordenarPor.isBlank()) ? "nombre" : ordenarPor;
        String direccionValida = (direccion == null || direccion.isBlank()) ? "asc" : direccion;
        return persistencePort.listarPaginado(pagina, tamanio, ordenValido, direccionValida)
                .flatMap(page -> enriquecerCapacidades(page.getCapacidades())
                        .map(capacidades -> new CapacidadPage(
                                capacidades,
                                page.getPaginaActual(),
                                page.getTotalPaginas(),
                                page.getTotalElementos()
                        )));
    }

    private Mono<List<Capacidad>> enriquecerCapacidades(List<Capacidad> capacidades) {
        return Flux.fromIterable(capacidades)
                .concatMap(this::enriquecerTecnologias)
                .collectList();
    }

    private Mono<Capacidad> enriquecerTecnologias(Capacidad capacidad) {
        return Flux.fromIterable(capacidad.getTecnologias())
                .concatMap(t -> tecnologiaServicePort.obtenerTecnologia(t.getId()))
                .collectList()
                .map(tecnologias -> {
                    capacidad.setTecnologias(tecnologias);
                    return capacidad;
                });
    }

    @Override
    public Mono<Capacidad> buscarPorId(Long id) {
        return persistencePort.buscarPorId(id)
                .flatMap(capacidad ->
                        Flux.fromIterable(capacidad.getTecnologias())
                                .concatMap(t -> tecnologiaServicePort.obtenerTecnologia(t.getId()))
                                .collectList()
                                .map(tecnologias -> {
                                    capacidad.setTecnologias(tecnologias);
                                    return capacidad;
                                })
                );
    }

}