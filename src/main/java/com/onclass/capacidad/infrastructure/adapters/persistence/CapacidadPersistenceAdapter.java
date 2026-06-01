package com.onclass.capacidad.infrastructure.adapters.persistence;

import com.onclass.capacidad.domain.model.Capacidad;
import com.onclass.capacidad.domain.model.CapacidadPage;
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

import java.util.Comparator;
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

    @Override
    public Mono<CapacidadPage> listarPaginado(int pagina, int tamanio, String ordenarPor, String direccion) {
        int offset = pagina * tamanio;

        return capacidadRepository.count()
                .flatMap(total -> {
                    int totalPaginas = (int) Math.ceil((double) total / tamanio);

                    return capacidadRepository.findAll()
                            .flatMap(entity ->
                                    capacidadTecnologiaRepository.findByCapacidadId(entity.getId())
                                            .map(rel -> new Tecnologia(rel.getTecnologiaId(), null))
                                            .collectList()
                                            .map(tecnologias -> {
                                                Capacidad cap = capacidadEntityMapper.toDomain(entity);
                                                cap.setTecnologias(tecnologias);
                                                return cap;
                                            })
                            )
                            .sort(getComparator(ordenarPor, direccion))
                            .skip(offset)
                            .take(tamanio)
                            .collectList()
                            .map(capacidades -> new CapacidadPage(
                                    capacidades,
                                    pagina,
                                    totalPaginas,
                                    total
                            ));
                });
    }

    private Comparator<Capacidad> getComparator(String ordenarPor, String direccion) {
        Comparator<Capacidad> comparator;
        if ("cantidadTecnologias".equalsIgnoreCase(ordenarPor)) {
            comparator = Comparator.comparingInt(c -> c.getTecnologias().size());
        } else {
            comparator = Comparator.comparing(c -> c.getNombre().toLowerCase());
        }
        if ("desc".equalsIgnoreCase(direccion)) {
            comparator = comparator.reversed();
        }
        return comparator;
    }

    @Override
    public Mono<Capacidad> buscarPorId(Long id) {
        return capacidadRepository.findById(id)
                .flatMap(entity ->
                        capacidadTecnologiaRepository.findByCapacidadId(entity.getId())
                                .map(rel -> new Tecnologia(rel.getTecnologiaId(), null))
                                .collectList()
                                .map(tecnologias -> {
                                    Capacidad cap = capacidadEntityMapper.toDomain(entity);
                                    cap.setTecnologias(tecnologias);
                                    return cap;
                                })
                );
    }
}