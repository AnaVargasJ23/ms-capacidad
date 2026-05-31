package com.onclass.capacidad.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CapacidadPage {
    private List<Capacidad> capacidades;
    private int paginaActual;
    private int totalPaginas;
    private long totalElementos;
}