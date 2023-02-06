package com.example.demo;

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
                .doOnSuccess(response -> {
                    String logMessage = String.format("%s %s %s", request.method(), request.url(), response.statusCode());
                    log.info(logMessage);
                });
    }
}
