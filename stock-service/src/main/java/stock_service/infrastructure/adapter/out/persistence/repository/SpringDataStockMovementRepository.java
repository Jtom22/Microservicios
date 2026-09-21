package stock_service.infrastructure.adapter.out.persistence.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
 
import reactor.core.publisher.Flux;
import stock_service.infrastructure.adapter.out.persistence.entity.StockMovementDocument;
 
public interface SpringDataStockMovementRepository extends ReactiveMongoRepository<StockMovementDocument, String> {
 
    Flux<StockMovementDocument> findByProductIdOrderByOccurredAtDesc(String productId);
}