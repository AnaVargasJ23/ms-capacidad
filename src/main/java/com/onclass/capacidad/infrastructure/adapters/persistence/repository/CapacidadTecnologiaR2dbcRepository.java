package com.onclass.capacidad.infrastructure.adapters.persistence.repository;

import com.onclass.capacidad.infrastructure.adapters.persistence.entity.CapacidadTecnologiaEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface CapacidadTecnologiaR2dbcRepository
        extends ReactiveCrudRepository<CapacidadTecnologiaEntity, Long> {

    Flux<CapacidadTecnologiaEntity> findByCapacidadId(Long capacidadId);

    @org.springframework.data.r2dbc.repository.Query("DELETE FROM capacidad_tecnologia WHERE capacidad_id = :capacidadId")
    reactor.core.publisher.Mono<Void> deleteByCapacidadId(Long capacidadId);

}