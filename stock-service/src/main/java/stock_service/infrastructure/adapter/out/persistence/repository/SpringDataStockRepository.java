package stock_service.infrastructure.adapter.out.persistence.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import stock_service.infrastructure.adapter.out.persistence.entity.StockDocument;

//  Repositorio
public interface SpringDataStockRepository extends ReactiveMongoRepository<StockDocument, String> {
}