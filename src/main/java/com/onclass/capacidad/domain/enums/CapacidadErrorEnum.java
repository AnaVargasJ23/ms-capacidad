package com.onclass.capacidad.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CapacidadErrorEnum {

    NOMBRE_OBLIGATORIO("CAP-001", "El nombre es obligatorio"),
    NOMBRE_MAX_50("CAP-002", "El nombre no puede superar 50 caracteres"),
    DESCRIPCION_OBLIGATORIA("CAP-003", "La descripción es obligatoria"),
    DESCRIPCION_MAX_90("CAP-004", "La descripción no puede superar 90 caracteres"),
    TECNOLOGIAS_MIN_3("CAP-005", "La capacidad debe tener mínimo 3 tecnologías"),
    TECNOLOGIAS_MAX_20("CAP-006", "La capacidad no puede tener más de 20 tecnologías"),
    TECNOLOGIAS_REPETIDAS("CAP-007", "La capacidad no puede tener tecnologías repetidas"),
    TECNOLOGIA_NO_EXISTE("CAP-008", "Una o más tecnologías no existen"),
    NOMBRE_DUPLICADO("CAP-009", "Ya existe una capacidad con ese nombre");

    private final String code;
    private final String message;
}