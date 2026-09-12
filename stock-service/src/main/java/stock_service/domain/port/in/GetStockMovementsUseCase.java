package stock_service.domain.port.in;

import reactor.core.publisher.Flux;
import stock_service.domain.model.StockMovement;

public interface GetStockMovementsUseCase {
        Flux<StockMovement> getMovements(String productId);

}
