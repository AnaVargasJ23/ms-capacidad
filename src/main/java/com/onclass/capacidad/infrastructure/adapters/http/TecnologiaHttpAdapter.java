package com.onclass.capacidad.infrastructure.adapters.http;

import com.onclass.capacidad.domain.constants.CapacidadConstants;
import com.onclass.capacidad.domain.model.Tecnologia;
import com.onclass.capacidad.domain.spi.ITecnologiaServicePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class TecnologiaHttpAdapter implements ITecnologiaServicePort {

    private final WebClient webClient;

    @Override
    public Mono<Boolean> existeTecnologia(Long id) {
        return webClient.get()
                .uri(CapacidadConstants.TECNOLOGIA_BASE_URL +
                        CapacidadConstants.TECNOLOGIA_ENDPOINT, id)
                .retrieve()
                .toBodilessEntity()
                .map(response -> response.getStatusCode().is2xxSuccessful())
                .onErrorResume(e -> {
                    log.error("Error verificando tecnología {}: {}", id, e.getMessage());
                    return Mono.just(false);
                });
    }

    public Mono<Tecnologia> obtenerTecnologia(Long id) {
        return webClient.get()
                .uri(CapacidadConstants.TECNOLOGIA_ENDPOINT, id)
                .retrieve()
                .bodyToMono(Tecnologia.class)
                .onErrorResume(e -> {
                    log.error("Error obteniendo tecnología {}: {}", id, e.getMessage());
                    return Mono.just(new Tecnologia(id, null));
                });
    }
}