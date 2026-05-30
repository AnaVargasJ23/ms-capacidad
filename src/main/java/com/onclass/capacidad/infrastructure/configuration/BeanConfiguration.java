package com.onclass.capacidad.infrastructure.configuration;

import com.onclass.capacidad.domain.api.ICapacidadServicePort;
import com.onclass.capacidad.domain.spi.ICapacidadPersistencePort;
import com.onclass.capacidad.domain.spi.ITecnologiaServicePort;
import com.onclass.capacidad.domain.usecase.CapacidadUseCase;
import com.onclass.capacidad.domain.constants.CapacidadConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class BeanConfiguration {
    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl(CapacidadConstants.TECNOLOGIA_BASE_URL)
                .build();
    }

    @Bean
    public ICapacidadServicePort capacidadServicePort(
            ICapacidadPersistencePort persistencePort,
            ITecnologiaServicePort tecnologiaServicePort) {
        return new CapacidadUseCase(persistencePort, tecnologiaServicePort);
    }
}