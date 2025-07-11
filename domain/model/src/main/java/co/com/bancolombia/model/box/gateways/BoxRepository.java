package co.com.bancolombia.model.box.gateways;

import co.com.bancolombia.model.box.Box;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BoxRepository {
    Flux<Box> findAll();
    Mono<Box> findById(String id);
    Mono<Box> save(Box box);
}
