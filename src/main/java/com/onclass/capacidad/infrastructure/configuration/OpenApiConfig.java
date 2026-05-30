package com.onclass.capacidad.infrastructure.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "MS Capacidad - On Class",
                version = "1.0",
                description = "Microservicio para gestión de capacidades"
        )
)
public class OpenApiConfig {
}