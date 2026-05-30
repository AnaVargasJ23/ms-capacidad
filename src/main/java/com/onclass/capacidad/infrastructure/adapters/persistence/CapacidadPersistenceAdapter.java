package com.onclass.capacidad.infrastructure.adapters.persistence;

import com.onclass.capacidad.domain.model.Capacidad;
import com.onclass.capacidad.domain.model.Tecnologia;
import com.onclass.capacidad.domain.spi.ICapacidadPersistencePort;
import com.onclass.capacidad.infrastructure.adapters.persistence.entity.CapacidadEntity;
import com.onclass.capacidad.infrastructure.adapters.persistence.entity.CapacidadTecnologiaEntity;
import com.onclass.capacidad.infrastructure.adapters.persistence.mapper.CapacidadEntityMapper;
import com.onclass.capacidad.infrastructure.adapters.persistence.repository.CapacidadR2dbcRepository;
import com.onclass.capacidad.infrastructure.adapters.persistence.repository.CapacidadTecnologiaR2dbcRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CapacidadPersistenceAdapter implements ICapacidadPersistencePort {

    private final CapacidadR2dbcRepository capacidadRepository;
    private final CapacidadTecnologiaR2dbcRepository capacidadTecnologiaRepository;
    private final CapacidadEntityMapper capacidadEntityMapper;

    @Override
    @Transactional
    public Mono<Capacidad> guardar(Capacidad capacidad) {
        return capacidadRepository.save(capacidadEntityMapper.toEntity(capacidad))
                .flatMap(savedEntity -> {
                    List<CapacidadTecnologiaEntity> relaciones = capacidad.getTecnologias()
                            .stream()
                            .map(t -> new CapacidadTecnologiaEntity(
                                    savedEntity.getId(),
                                    t.getId()))
                            .toList();
                    return capacidadTecnologiaRepository.saveAll(relaciones)
                            .then(Mono.just(capacidadEntityMapper.toDomain(savedEntity)));
                })
                .map(cap -> {
                    cap.setTecnologias(capacidad.getTecnologias());
                    return cap;
                });
    }

    @Override
    public Mono<Boolean> existePorNombre(String nombre) {
        return capacidadRepository.existsByNombre(nombre);
    }

    @Override
    public Flux<Capacidad> listarTodas() {
        return capacidadRepository.findAll()
                .flatMap(entity ->
                        capacidadTecnologiaRepository.findByCapacidadId(entity.getId())
                                .map(rel -> new Tecnologia(rel.getTecnologiaId(), null))
                                .collectList()
                                .map(tecnologias -> {
                                    Capacidad capacidad = capacidadEntityMapper.toDomain(entity);
                                    capacidad.setTecnologias(tecnologias);
                                    return capacidad;
                                })
                );
    }
}