package com.onclass.capacidad.domain.spi;

import reactor.core.publisher.Mono;

public interface ITecnologiaServicePort {
    Mono<Boolean> existeTecnologia(Long id);
}