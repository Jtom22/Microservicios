package stock_service.domain.port.in;

import reactor.core.publisher.Mono;
import stock_service.domain.model.OrderCreatedEvent;
 
/**
 * Puerto de ENTRADA (driving port): define qué caso de uso expone el dominio.
 * El adapter de Kafka (infrastructure) depende de esta interfaz, nunca al revés.
 */
public interface DecreaseStockUseCase {
 
    Mono<Void> handle(OrderCreatedEvent event);
}
 
