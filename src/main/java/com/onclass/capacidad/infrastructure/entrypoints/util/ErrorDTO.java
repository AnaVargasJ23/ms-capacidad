package com.onclass.capacidad.infrastructure.entrypoints.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorDTO {
    private String code;
    private String message;
}