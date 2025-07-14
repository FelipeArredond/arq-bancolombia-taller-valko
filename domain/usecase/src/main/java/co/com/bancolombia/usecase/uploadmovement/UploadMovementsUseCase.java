package co.com.bancolombia.usecase.uploadmovement;

import co.com.bancolombia.model.events.gateways.EventsGateway;
import co.com.bancolombia.model.movement.Movement;
import co.com.bancolombia.model.movement.gateways.MovementRepository;
import co.com.bancolombia.usecase.getboxbyid.GetboxbyidUseCase;
import reactor.core.publisher.Mono;

import java.util.UUID;

public class UploadMovementsUseCase {

    private final GetboxbyidUseCase getboxbyidUseCase;
    private final MovementRepository repository;

    public UploadMovementsUseCase(GetboxbyidUseCase getboxbyidUseCase, MovementRepository repository){
        this.getboxbyidUseCase = getboxbyidUseCase;
        this.repository = repository;
    }

    public Mono<Movement> createMovement(Movement movement, String reqBoxId) {
        movement.setId(UUID.randomUUID().toString());
        return Mono.just(reqBoxId)
                .flatMap(reqBoxIdMono -> {
                    if(!reqBoxId.equals(movement.getBoxId())) {
                        return Mono.error(new IllegalStateException("Request box id don't match with movement box id"));
                    }
                    return Mono.just(movement);
                })
                .flatMap(validatedMovement -> getboxbyidUseCase.
                        getBoxById(validatedMovement.getBoxId()))
                .flatMap(box -> repository.save(movement))
                .switchIfEmpty(Mono.error(new IllegalStateException("Box Id" +
                        movement.getBoxId() +
                        " don't exist")));
    }

}
