package com.onclass.capacidad.infrastructure.adapters.persistence.repository;

import com.onclass.capacidad.infrastructure.adapters.persistence.entity.CapacidadTecnologiaEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface CapacidadTecnologiaR2dbcRepository
        extends ReactiveCrudRepository<CapacidadTecnologiaEntity, Long> {
    Flux<CapacidadTecnologiaEntity> findByCapacidadId(Long capacidadId);
}