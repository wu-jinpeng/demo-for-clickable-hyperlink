package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
public class Controller {
    @Autowired
    private Connector connector;

    @GetMapping("/users")
    public Mono<ResponseEntity<List<User>>> getUsers() {
        return Flux.just(
                        "https://api.val.town/eval/@fake.search?query=(key:value)",
                        "https://api.val.town/eval/@fake.search?query=((key:value) AND (a:b))",
                        "https://api.val.town/eval/@fake.search?query=((key:value) OR (a:b))",
                        "https://api.val.town/eval/@fake.search?query=((key:value AND a:b) OR (c:d))",
                        "https://api.val.town/eval/@fake.search?query=((key:value) AND (a:b) AND (c:d))"
                ).flatMap(connector::getUser)
                .collectList()
                .map(users -> ResponseEntity.ok().body(users));
    }
}
