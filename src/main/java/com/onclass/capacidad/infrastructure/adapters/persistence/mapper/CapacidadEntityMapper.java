package com.onclass.capacidad.infrastructure.adapters.persistence.mapper;

import com.onclass.capacidad.domain.model.Capacidad;
import com.onclass.capacidad.infrastructure.adapters.persistence.entity.CapacidadEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CapacidadEntityMapper {
    @Mapping(target = "tecnologias", ignore = true)
    Capacidad toDomain(CapacidadEntity entity);

    @Mapping(target = "id", ignore = true)
    CapacidadEntity toEntity(Capacidad capacidad);
}