package co.com.bancolombia.usecase.findmovement;

import co.com.bancolombia.model.movement.Movement;
import co.com.bancolombia.model.movement.gateways.MovementRepository;
import reactor.core.publisher.Mono;

public class FindmovementUseCase {

    private final MovementRepository repository;

    public FindmovementUseCase(MovementRepository repository){
        this.repository = repository;
    }

    public Mono<Movement> findMovementById(String id) {
        return repository.findById(id);
    }

}
