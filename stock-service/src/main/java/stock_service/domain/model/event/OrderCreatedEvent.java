package stock_service.domain.model.event;

import java.time.Instant;
import java.util.List;
 
//  Representa el evento de crear un pedido, tal como lo pide en order-service.
//  Es un DTO de dominio para estructurar datos, son las ordenes de movimientos del stock que luego se
//  consumen en el service
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
