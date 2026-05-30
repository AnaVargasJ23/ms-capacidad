package com.onclass.capacidad.infrastructure.entrypoints.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CapacidadRequest {
    private String nombre;
    private String descripcion;
    private List<TecnologiaIdRequest> tecnologias;
}