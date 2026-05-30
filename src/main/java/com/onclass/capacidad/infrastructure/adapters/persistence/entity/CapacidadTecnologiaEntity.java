package com.onclass.capacidad.infrastructure.adapters.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Table;

@Table("capacidad_tecnologia")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CapacidadTecnologiaEntity {
    private Long capacidadId;
    private Long tecnologiaId;
}