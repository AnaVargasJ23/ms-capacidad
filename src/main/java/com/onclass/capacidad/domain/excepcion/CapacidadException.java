package com.onclass.capacidad.domain.excepcion;

import lombok.Getter;

@Getter
public class CapacidadException extends RuntimeException {
    private final String code;
    private final String message;

    public CapacidadException(String code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }
}
