package co.com.bancolombia.model.error.gateways;

import co.com.bancolombia.model.error.Error;
import reactor.core.publisher.Mono;

public interface ErrorRepository {

    Mono<Void> saveError(Error error);

}
