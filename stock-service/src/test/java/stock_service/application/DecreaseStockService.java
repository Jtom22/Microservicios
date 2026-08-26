package stock_service.application;

import org.springframework.stereotype.Service;
 
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import stock_service.domain.model.OrderCreatedEvent;
import stock_service.domain.model.StockItem;
import stock_service.domain.port.in.DecreaseStockUseCase;
import stock_service.domain.port.out.StockRepositoryPort;
 

// Esta clase SÍ vive en application (no en domain) porque orquesta: llama al
// puerto de salida, coordina varios productos de un mismo pedido. La regla de
// negocio en sí (no permitir stock negativo) vive dentro de StockItem.decrease(),
// en el dominio puro — aqui solo la invocamos.
@Service
public class DecreaseStockService implements DecreaseStockUseCase {
 
    private final StockRepositoryPort stockRepository;
 
    public DecreaseStockService(StockRepositoryPort stockRepository) {
        this.stockRepository = stockRepository;
    }
 
    //Genera cola para procesar cada uno de los objetos(iterables) de manera secuencial asincrona(no bloquea)
    // y entonces reduce el stock de ese objeto
    //public Mono<Void> handle(...)->Espera promesa asincrona
    @Override
    public Mono<Void> handle(OrderCreatedEvent event) {
        return Flux.fromIterable(event.items())
                .concatMap(this::decreaseOneItem)
                .then();//Señal de exito
    }

 
    private Mono<StockItem> decreaseOneItem(OrderCreatedEvent.OrderItem item) {
        return stockRepository.findByProductId(item.productId())
                .switchIfEmpty(Mono.error(new IllegalStateException(
                        "No existe stock registrado para el producto " + item.productId())))
                .doOnNext(stockItem -> stockItem.decrease(item.quantity()))
                .flatMap(stockRepository::save);
    }
}
