package com.onclass.capacidad.infrastructure.entrypoints.mapper;

import com.onclass.capacidad.domain.model.Capacidad;
import com.onclass.capacidad.domain.model.Tecnologia;
import com.onclass.capacidad.infrastructure.entrypoints.dto.CapacidadRequest;
import com.onclass.capacidad.infrastructure.entrypoints.dto.CapacidadResponse;
import com.onclass.capacidad.infrastructure.entrypoints.dto.TecnologiaIdRequest;
import com.onclass.capacidad.infrastructure.entrypoints.dto.TecnologiaResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CapacidadMapper {
    @Mapping(target = "id", ignore = true)
    Capacidad toDomain(CapacidadRequest request);
    Tecnologia toDomain(TecnologiaIdRequest request);
    CapacidadResponse toResponse(Capacidad capacidad);
    TecnologiaResponse toResponse(Tecnologia tecnologia);
}