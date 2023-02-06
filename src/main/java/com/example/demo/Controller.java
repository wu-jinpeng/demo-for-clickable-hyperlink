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
        return Flux.merge(
                connector.getUser("https://api.val.town/eval/@fake.search?query=(key:value)"),
                connector.getUser("https://api.val.town/eval/@fake.search?query=((key:value) AND (a:b))"),
                connector.getUser("https://api.val.town/eval/@fake.search?query=((key:value) OR (a:b))"),
                connector.getUser("https://api.val.town/eval/@fake.search?query=((key:value AND a:b) OR (c:d))"),
                connector.getUser("https://api.val.town/eval/@fake.search?query=((key:value) AND (a:b) AND (c:d))")
        ).collectList().map(users -> ResponseEntity.ok().body(users));
    }
}
