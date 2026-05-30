package com.onclass.capacidad.infrastructure.adapters.persistence.repository;

import com.onclass.capacidad.infrastructure.adapters.persistence.entity.CapacidadEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface CapacidadR2dbcRepository
        extends ReactiveCrudRepository<CapacidadEntity, Long> {
    Mono<Boolean> existsByNombre(String nombre);
}