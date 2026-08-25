package stock_service.domain.model;

import java.time.Instant;
import java.util.List;
 
//  Representa el evento de crear un pedido, tal como lo pide en order-service.
// Es un DTO
public record OrderCreatedEvent(
    String orderId,
    String customerId,
    List<OrderItem> items,
    Instant createdAt
) {
    public record OrderItem(
        String productId,
        int quantity
    ) {}
}
