package com.onclass.capacidad.domain.spi;

import com.onclass.capacidad.domain.model.Capacidad;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ICapacidadPersistencePort {
    Mono<Capacidad> guardar(Capacidad capacidad);
    Mono<Boolean> existePorNombre(String nombre);
    Flux<Capacidad> listarTodas();
}