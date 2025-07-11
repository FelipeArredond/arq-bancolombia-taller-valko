package co.com.bancolombia.usecase.listallboxes;

import co.com.bancolombia.model.box.Box;
import co.com.bancolombia.model.box.gateways.BoxRepository;
import reactor.core.publisher.Flux;

public class ListallboxesUseCase {

    private final BoxRepository repository;

    public ListallboxesUseCase(BoxRepository repository){
        this.repository = repository;
    }

    public Flux<Box> listAllBoxes() {
        return repository.findAll();
    }

}
