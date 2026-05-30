package com.onclass.capacidad.infrastructure.entrypoints.mapper;

import com.onclass.capacidad.domain.model.Capacidad;
import com.onclass.capacidad.domain.model.Tecnologia;
import com.onclass.capacidad.infrastructure.entrypoints.dto.CapacidadRequest;
import com.onclass.capacidad.infrastructure.entrypoints.dto.CapacidadResponse;
import com.onclass.capacidad.infrastructure.entrypoints.dto.TecnologiaIdRequest;
import com.onclass.capacidad.infrastructure.entrypoints.dto.TecnologiaResponse;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-05-29T16:47:09-0500",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-9.3.0.jar, environment: Java 21.0.11 (Oracle Corporation)"
)
@Component
public class CapacidadMapperImpl implements CapacidadMapper {

    @Override
    public Capacidad toDomain(CapacidadRequest request) {
        if ( request == null ) {
            return null;
        }

        Capacidad capacidad = new Capacidad();

        capacidad.setNombre( request.getNombre() );
        capacidad.setDescripcion( request.getDescripcion() );
        capacidad.setTecnologias( tecnologiaIdRequestListToTecnologiaList( request.getTecnologias() ) );

        return capacidad;
    }

    @Override
    public Tecnologia toDomain(TecnologiaIdRequest request) {
        if ( request == null ) {
            return null;
        }

        Tecnologia tecnologia = new Tecnologia();

        tecnologia.setId( request.getId() );

        return tecnologia;
    }

    @Override
    public CapacidadResponse toResponse(Capacidad capacidad) {
        if ( capacidad == null ) {
            return null;
        }

        CapacidadResponse capacidadResponse = new CapacidadResponse();

        capacidadResponse.setId( capacidad.getId() );
        capacidadResponse.setNombre( capacidad.getNombre() );
        capacidadResponse.setDescripcion( capacidad.getDescripcion() );
        capacidadResponse.setTecnologias( tecnologiaListToTecnologiaResponseList( capacidad.getTecnologias() ) );

        return capacidadResponse;
    }

    @Override
    public TecnologiaResponse toResponse(Tecnologia tecnologia) {
        if ( tecnologia == null ) {
            return null;
        }

        TecnologiaResponse tecnologiaResponse = new TecnologiaResponse();

        tecnologiaResponse.setId( tecnologia.getId() );
        tecnologiaResponse.setNombre( tecnologia.getNombre() );

        return tecnologiaResponse;
    }

    protected List<Tecnologia> tecnologiaIdRequestListToTecnologiaList(List<TecnologiaIdRequest> list) {
        if ( list == null ) {
            return null;
        }

        List<Tecnologia> list1 = new ArrayList<Tecnologia>( list.size() );
        for ( TecnologiaIdRequest tecnologiaIdRequest : list ) {
            list1.add( toDomain( tecnologiaIdRequest ) );
        }

        return list1;
    }

    protected List<TecnologiaResponse> tecnologiaListToTecnologiaResponseList(List<Tecnologia> list) {
        if ( list == null ) {
            return null;
        }

        List<TecnologiaResponse> list1 = new ArrayList<TecnologiaResponse>( list.size() );
        for ( Tecnologia tecnologia : list ) {
            list1.add( toResponse( tecnologia ) );
        }

        return list1;
    }
}
