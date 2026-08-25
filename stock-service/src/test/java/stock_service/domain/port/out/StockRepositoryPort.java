package stock_service.domain.port.out;

import reactor.core.publisher.Mono;
import stock_service.domain.model.StockItem;
 
/**
 * Puerto de SALIDA (driven port): lo que el dominio necesita para persistir
 * stock, sin saber que por debajo hay MongoDB. El adapter de persistencia
 * (infrastructure) implementa esta interfaz.
 */
public interface StockRepositoryPort {
 
    Mono<StockItem> findByProductId(String productId);
 
    Mono<StockItem> save(StockItem stockItem);
}
 
