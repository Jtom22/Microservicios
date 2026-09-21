package stock_service.infrastructure.adapter.out.persistence;

import org.springframework.stereotype.Component;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import stock_service.domain.model.StockItem;
import stock_service.domain.port.out.StockRepositoryPort;
import stock_service.infrastructure.adapter.out.persistence.entity.StockDocument;
import stock_service.infrastructure.adapter.out.persistence.repository.SpringDataStockRepository;

// Implementa el puerto de salida definido por el dominio. Es la ÚNICA clase
// de todo el proyecto que sabe que "por debajo hay MongoDB" — ni el dominio
// ni el caso de uso en application/ tienen ni un import de Spring Data Mongo.
//Adaptador del Repository para Mongo
//su funcion es mappear entre la entidad dentro de dominio y fuera y utilizar las funiones del repository 

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

    @Override
    public Mono<Boolean> existsByProductId(String productId) {
        return springDataRepository.existsById(productId);
    }

    @Override
    public Flux<StockItem> findAll() {
        return springDataRepository.findAll()
                .map(this::toDomain);
    }

    // Mapperar entre la entidad de Spring y la entidad de java puro tanto toDomain
    // como toDocument
    private StockItem toDomain(StockDocument document) {
        return new StockItem(document.getProductId(), document.getQuantity());
    }

    private StockDocument toDocument(StockItem domain) {
        return new StockDocument(domain.getProductId(), domain.getQuantity());
    }

}
