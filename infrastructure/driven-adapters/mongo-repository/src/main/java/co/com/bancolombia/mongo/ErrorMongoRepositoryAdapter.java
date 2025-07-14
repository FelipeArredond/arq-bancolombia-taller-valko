package co.com.bancolombia.mongo;

import co.com.bancolombia.model.error.Error;
import co.com.bancolombia.model.error.gateways.ErrorRepository;
import co.com.bancolombia.model.movement.Movement;
import co.com.bancolombia.model.movement.gateways.MovementRepository;
import co.com.bancolombia.mongo.helper.AdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class ErrorMongoRepositoryAdapter extends AdapterOperations<Error, ErrorData, String, ErrorMongoDBRepository>
 implements ErrorRepository
{

    public ErrorMongoRepositoryAdapter(ErrorMongoDBRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, Error.class));
    }

    @Override
    public Mono<Void> saveError(Error error) {
        return Mono.just(error)
                .map(this::toData)
                .flatMap(errorData -> repository.save(errorData))
                .then();
    }

}
