package com.example.demo;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import static reactor.core.publisher.Flux.fromIterable;

@Component
public class Connector {
    private final WebClient webClient;

    public Connector(LoggingInterceptor loggingInterceptor) {
        this.webClient = WebClient.builder()
                .filter(loggingInterceptor)
                .build();
    }

    public Flux<User> getUser(String uri) {
        return webClient.get()
                .uri(uri)
                .retrieve().bodyToMono(JsonNode.class)
                .flatMapMany(jsonNode -> fromIterable(jsonNode.path("data")))
                .map(jsonNode -> new User(jsonNode.get("id").asText()));
    }
}
