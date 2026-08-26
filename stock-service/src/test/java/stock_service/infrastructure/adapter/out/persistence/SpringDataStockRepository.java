package stock_service.infrastructure.adapter.out.persistence;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

//  Repositorio
public interface SpringDataStockRepository extends ReactiveMongoRepository<StockDocument, String> {
}