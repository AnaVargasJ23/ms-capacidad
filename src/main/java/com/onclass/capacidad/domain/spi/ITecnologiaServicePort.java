package com.onclass.capacidad.domain.spi;

import com.onclass.capacidad.domain.model.Tecnologia;
import reactor.core.publisher.Mono;

public interface ITecnologiaServicePort {
    Mono<Boolean> existeTecnologia(Long id);

    Mono<Tecnologia> obtenerTecnologia(Long id);
}