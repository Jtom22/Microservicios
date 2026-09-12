package stock_service.infrastructure.adapter.out.persistence;

import org.springframework.stereotype.Component;
 
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import stock_service.domain.model.MovementType;
import stock_service.domain.model.StockMovement;
import stock_service.domain.port.out.StockMovementRepositoryPort;
import stock_service.infrastructure.adapter.out.persistence.entity.StockMovementDocument;
import stock_service.infrastructure.adapter.out.persistence.repository.SpringDataStockMovementRepository;
 
@Component
public class MongoStockMovementRepositoryAdapter implements StockMovementRepositoryPort {
 
    private final SpringDataStockMovementRepository springDataRepository;
 
    public MongoStockMovementRepositoryAdapter(SpringDataStockMovementRepository springDataRepository) {
        this.springDataRepository = springDataRepository;
    }
 
    @Override
    public Mono<StockMovement> save(StockMovement movement) {
        StockMovementDocument document = toDocument(movement);
        return springDataRepository.save(document)
                .map(this::toDomain);
    }
 
    @Override
    public Flux<StockMovement> findByProductId(String productId) {
        return springDataRepository.findByProductIdOrderByOccurredAtDesc(productId)
                .map(this::toDomain);
    }
 
    private StockMovement toDomain(StockMovementDocument doc) {
        return new StockMovement(
                doc.getId(),
                doc.getProductId(),
                MovementType.valueOf(doc.getType()),
                doc.getAmount(),
                doc.getResultingQuantity(),
                doc.getReason(),
                doc.getOccurredAt()
        );
    }
 
    private StockMovementDocument toDocument(StockMovement domain) {
        return new StockMovementDocument(
                domain.getId(),
                domain.getProductId(),
                domain.getType().name(),
                domain.getAmount(),
                domain.getResultingQuantity(),
                domain.getReason(),
                domain.getOccurredAt()
        );
    }
}
 
