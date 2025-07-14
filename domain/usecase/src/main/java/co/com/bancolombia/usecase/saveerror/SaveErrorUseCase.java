package co.com.bancolombia.usecase.saveerror;

import co.com.bancolombia.model.error.Error;
import co.com.bancolombia.model.error.gateways.ErrorRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class SaveErrorUseCase {

    private final ErrorRepository repository;

    public Mono<Void> saveError(Error error) {
        return repository.saveError(error);
    }

}
