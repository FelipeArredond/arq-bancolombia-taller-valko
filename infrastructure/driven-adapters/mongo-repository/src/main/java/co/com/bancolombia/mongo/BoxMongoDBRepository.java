package co.com.bancolombia.mongo;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;

public interface BoxMongoDBRepository extends ReactiveMongoRepository<BoxData, String>, ReactiveQueryByExampleExecutor<BoxData> {
}
