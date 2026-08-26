package stock_service.infrastructure.adapter.out.persistence;

import org.springframework.stereotype.Component;

import reactor.core.publisher.Mono;
import stock_service.domain.model.StockItem;
import stock_service.domain.port.out.StockRepositoryPort;

// Implementa el puerto de salida definido por el dominio. Es la ÚNICA clase
// de todo el proyecto que sabe que "por debajo hay MongoDB" — ni el dominio
// ni el caso de uso en application/ tienen ni un import de Spring Data Mongo.

@Component
public class MongoStockRepositoryAdapter implements StockRepositoryPort {
 
    private final SpringDataStockRepository springDataRepository;
 
    public MongoStockRepositoryAdapter(SpringDataStockRepository springDataRepository) {
        this.springDataRepository = springDataRepository;
    }
 
    @Override
    public Mono<StockItem> findByProductId(String productId) {
        return springDataRepository.findById(productId)
                .map(this::toDomain);
    }
 
    @Override
    public Mono<StockItem> save(StockItem stockItem) {
        StockDocument document = toDocument(stockItem);
        return springDataRepository.save(document)
                .map(this::toDomain);
    }
 
    private StockItem toDomain(StockDocument document) {
        return new StockItem(document.getProductId(), document.getQuantity());
    }
 
    private StockDocument toDocument(StockItem domain) {
        return new StockDocument(domain.getProductId(), domain.getQuantity());
    }
}
 
