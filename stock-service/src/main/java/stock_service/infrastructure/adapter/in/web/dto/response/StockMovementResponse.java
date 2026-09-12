package stock_service.infrastructure.adapter.in.web.dto.response;

import java.time.Instant;

import stock_service.domain.model.StockMovement;
 
public record StockMovementResponse(
    String id,
    String productId,
    String type,
    int amount,
    int resultingQuantity,
    String reason,
    Instant occurredAt
) {
    public static StockMovementResponse from(StockMovement movement) {
        return new StockMovementResponse(
                movement.getId(),
                movement.getProductId(),
                movement.getType().name(),
                movement.getAmount(),
                movement.getResultingQuantity(),
                movement.getReason(),
                movement.getOccurredAt()
        );
    }
}
