package stock_service.application;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import stock_service.domain.model.event.OrderCreatedEvent;
import stock_service.domain.exception.StockAlreadyExistsException;
import stock_service.domain.exception.StockNotFoundException;
import stock_service.domain.model.MovementType;
import stock_service.domain.model.StockItem;
import stock_service.domain.model.StockMovement;
import stock_service.domain.port.in.StockUseCase;
import stock_service.domain.port.out.StockMovementRepositoryPort;
import stock_service.domain.port.out.StockRepositoryPort;

// Esta clase SÍ vive en application (no en domain) porque orquesta: llama al
// puerto de salida, coordina varios productos de un mismo pedido. La regla de
// negocio en sí (no permitir stock negativo) vive dentro de StockItem.decrease(),
// en el dominio puro — aqui solo la invocamos.
public class StockService implements StockUseCase {
 
    private final StockRepositoryPort stockRepository;
    private final StockMovementRepositoryPort movementRepository;
 
    public StockService(StockRepositoryPort stockRepository,
                         StockMovementRepositoryPort movementRepository) {
        this.stockRepository = stockRepository;
        this.movementRepository = movementRepository;
    }
 
    // ---- Disparado por Kafka ----
 
    @Override
    public Mono<Void> handle(OrderCreatedEvent event) {
        return Flux.fromIterable(event.items())
                .concatMap(item -> decreaseOneItem(item, event.orderId()))
                .then();
    }
 
    private Mono<StockItem> decreaseOneItem(OrderCreatedEvent.OrderItem item, String orderId) {
        return stockRepository.findByProductId(item.productId())
                .switchIfEmpty(Mono.error(new StockNotFoundException(item.productId())))
                .doOnNext(stockItem -> stockItem.decrease(item.quantity()))
                .flatMap(stockRepository::save)
                .flatMap(saved -> recordMovement(saved, MovementType.DECREASE, item.quantity(), "order:" + orderId)
                        .thenReturn(saved));
    }
 
    // ---- Disparados por REST ----
 
    @Override
    public Mono<StockItem> create(String productId, int initialQuantity) {
        return stockRepository.existsByProductId(productId)
                .flatMap(exists -> {
                    if (exists) {
                        return Mono.error(new StockAlreadyExistsException(productId));
                    }
                    StockItem newItem = new StockItem(productId, 0);
                    if (initialQuantity > 0) {
                        newItem.increase(initialQuantity);
                    }
                    return stockRepository.save(newItem)
                            .flatMap(saved -> recordMovement(saved, MovementType.INCREASE, initialQuantity, "initial-stock")
                                    .thenReturn(saved));
                });
    }
 
    @Override
    public Mono<StockItem> replenish(String productId, int amount) {
        return stockRepository.findByProductId(productId)
                .switchIfEmpty(Mono.error(new StockNotFoundException(productId)))
                .doOnNext(stockItem -> stockItem.increase(amount))
                .flatMap(stockRepository::save)
                .flatMap(saved -> recordMovement(saved, MovementType.INCREASE, amount, "manual-replenish")
                        .thenReturn(saved));
    }
 
    @Override
    public Mono<StockItem> getByProductId(String productId) {
        return stockRepository.findByProductId(productId)
                .switchIfEmpty(Mono.error(new StockNotFoundException(productId)));
    }
 
    @Override
    public Flux<StockItem> getAll() {
        return stockRepository.findAll();
    }
 
    @Override
    public Flux<StockMovement> getMovements(String productId) {
        return movementRepository.findByProductId(productId);
    }
 
    // ---- Helper compartido ----
 
    private Mono<StockMovement> recordMovement(StockItem stockItem, MovementType type, int amount, String reason) {
        if (amount <= 0) {
            return Mono.empty();
        }
        StockMovement movement = StockMovement.newMovement(
                stockItem.getProductId(), type, amount, stockItem.getQuantity(), reason);
        return movementRepository.save(movement);
    }
}
