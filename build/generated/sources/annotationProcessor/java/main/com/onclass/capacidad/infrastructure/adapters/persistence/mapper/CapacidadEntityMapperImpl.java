package com.onclass.capacidad.infrastructure.adapters.persistence.mapper;

import com.onclass.capacidad.domain.model.Capacidad;
import com.onclass.capacidad.infrastructure.adapters.persistence.entity.CapacidadEntity;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-05-29T16:47:09-0500",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-9.3.0.jar, environment: Java 21.0.11 (Oracle Corporation)"
)
@Component
public class CapacidadEntityMapperImpl implements CapacidadEntityMapper {

    @Override
    public Capacidad toDomain(CapacidadEntity entity) {
        if ( entity == null ) {
            return null;
        }

        Capacidad capacidad = new Capacidad();

        capacidad.setId( entity.getId() );
        capacidad.setNombre( entity.getNombre() );
        capacidad.setDescripcion( entity.getDescripcion() );

        return capacidad;
    }

    @Override
    public CapacidadEntity toEntity(Capacidad capacidad) {
        if ( capacidad == null ) {
            return null;
        }

        CapacidadEntity capacidadEntity = new CapacidadEntity();

        capacidadEntity.setNombre( capacidad.getNombre() );
        capacidadEntity.setDescripcion( capacidad.getDescripcion() );

        return capacidadEntity;
    }
}
