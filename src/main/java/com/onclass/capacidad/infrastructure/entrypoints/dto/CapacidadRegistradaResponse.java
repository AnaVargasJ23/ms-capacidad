package com.onclass.capacidad.infrastructure.entrypoints.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CapacidadRegistradaResponse {
    private Long id;
    private String nombre;
    private String descripcion;
    private List<TecnologiaResponse> tecnologias;
    private String mensaje;
}
