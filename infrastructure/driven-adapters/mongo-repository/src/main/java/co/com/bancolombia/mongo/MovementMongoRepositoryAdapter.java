package co.com.bancolombia.mongo;

import co.com.bancolombia.model.box.Box;
import co.com.bancolombia.model.movement.Movement;
import co.com.bancolombia.model.movement.gateways.MovementRepository;
import co.com.bancolombia.mongo.helper.AdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class MovementMongoRepositoryAdapter extends AdapterOperations<Movement, MovementData, String, MovementMongoDBRepository>
 implements MovementRepository
{

    public MovementMongoRepositoryAdapter(MovementMongoDBRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, Movement.class));
    }

    @Override
    public Mono<Movement> save(Movement movement) {
        return Mono.just(movement)
                .map(this::toData)
                .flatMap(movementData -> repository.save(movementData)
                        .map(this::toEntity)
                );
    }

    @Override
    public Mono<Movement> findById(String id) {
        return repository.findById(id).map(this::toEntity);
    }

}
