package stock_service.domain.port.in;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import stock_service.domain.model.StockItem;
import stock_service.domain.model.StockMovement;
import stock_service.domain.model.event.OrderCreatedEvent;
 
/**
 * Puerto de ENTRADA (driving port): define qué caso de uso expone el dominio.
 * El adapter de Kafka (infrastructure) depende de esta interfaz, nunca al revés.
 */
public interface StockUseCase {
 
    // Disparado por KafkaOrderCreatedConsumer
    Mono<Void> handle(OrderCreatedEvent event);
 
    // Disparados por StockController (REST)
    Mono<StockItem> create(String productId, int initialQuantity);
 
    Mono<StockItem> replenish(String productId, int amount);
 
    Mono<StockItem> getByProductId(String productId);
 
    Flux<StockItem> getAll();
 
    Flux<StockMovement> getMovements(String productId);
}