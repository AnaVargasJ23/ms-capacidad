package com.onclass.capacidad.domain.spi;

import com.onclass.capacidad.domain.model.Capacidad;
import com.onclass.capacidad.domain.model.CapacidadPage;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ICapacidadPersistencePort {
    Mono<Capacidad> guardar(Capacidad capacidad);
    Mono<Boolean> existePorNombre(String nombre);
    Flux<Capacidad> listarTodas();
    Mono<CapacidadPage> listarPaginado(int pagina, int tamanio, String ordenarPor, String direccion);
}