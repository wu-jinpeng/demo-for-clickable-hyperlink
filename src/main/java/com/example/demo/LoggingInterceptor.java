package com.example.demo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import reactor.core.publisher.Mono;

@Component
public class LoggingInterceptor implements ExchangeFilterFunction {
    private static final Logger log = LoggerFactory.getLogger(LoggingInterceptor.class);

    @Override
    public Mono<ClientResponse> filter(ClientRequest request, ExchangeFunction next) {
        return next.exchange(request)
                .flatMap(response -> response.bodyToMono(String.class)
                        .doOnNext(responseBody -> {
                            JsonNode jsonNode;
                            try {
                                jsonNode = new ObjectMapper().readTree(responseBody);
                            } catch (JsonProcessingException e) {
                                throw new RuntimeException(e);
                            }

                            ArrayNode data = (ArrayNode) jsonNode.get("data");
                            String logMessage = String.format("%s %s %s %s", request.method(), request.url(), response.statusCode(), data.isEmpty() ? "❌" : "✅");
                            log.info(logMessage);
                        })
                        .flatMap(responseBody -> Mono.just(response.mutate().body(responseBody).build())));
    }
}
