package com.onclass.capacidad.domain.api;

import com.onclass.capacidad.domain.model.Capacidad;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ICapacidadServicePort {
    Mono<Capacidad> registrar(Capacidad capacidad);
    Flux<Capacidad> listarTodas();
}