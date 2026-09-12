package stock_service.domain.port.out;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import stock_service.domain.model.StockMovement;

public interface StockMovementRepositoryPort {
 
    Mono<StockMovement> save(StockMovement movement);
 
    Flux<StockMovement> findByProductId(String productId);
}
