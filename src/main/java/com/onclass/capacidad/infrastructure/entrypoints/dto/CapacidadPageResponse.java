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
public class CapacidadPageResponse {
    private List<CapacidadResponse> capacidades;
    private int paginaActual;
    private int totalPaginas;
    private long totalElementos;
}