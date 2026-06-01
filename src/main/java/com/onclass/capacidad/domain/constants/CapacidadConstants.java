package com.onclass.capacidad.domain.constants;

public class CapacidadConstants {

    private CapacidadConstants() {}

    // URLs de ms-tecnologia
    public static final String TECNOLOGIA_BASE_URL = "http://localhost:8080";
    public static final String TECNOLOGIA_ENDPOINT = "/api/v1/tecnologias/{id}";
    public static final String TECNOLOGIA_ELIMINAR_ENDPOINT = "/api/v1/tecnologias/{id}";

    // Límites de negocio
    public static final int TECNOLOGIAS_MIN = 3;
    public static final int TECNOLOGIAS_MAX = 20;
    public static final int NOMBRE_MAX_LENGTH = 50;
    public static final int DESCRIPCION_MAX_LENGTH = 90;
}