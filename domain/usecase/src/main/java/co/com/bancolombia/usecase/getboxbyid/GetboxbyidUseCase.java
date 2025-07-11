package co.com.bancolombia.usecase.getboxbyid;

import co.com.bancolombia.model.box.Box;
import co.com.bancolombia.model.box.gateways.BoxRepository;
import reactor.core.publisher.Mono;

public class GetboxbyidUseCase {

    private final BoxRepository repository;

    public GetboxbyidUseCase(BoxRepository repository){
        this.repository = repository;
    }

    public Mono<Box> getBoxById(String id) {
        return repository.findById(id);
    }

}
