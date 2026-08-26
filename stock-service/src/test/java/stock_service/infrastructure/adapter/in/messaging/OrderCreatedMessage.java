package stock_service.infrastructure.adapter.in.messaging;


import java.time.Instant;
import java.util.List;


// Espejo del JSON que llega por Kafka. Podría parecer redundante con
// OrderCreatedEvent del dominio, pero a propósito los mantenemos separados:
// si order-service cambia el formato del JSON (agrega un campo, renombra
// uno), este DTO absorbe el cambio sin tocar el dominio. Jackson necesita
// anotaciones/constructor sin argumentos aquí; el record de dominio se
// mantiene limpio de esa preocupación.

public class OrderCreatedMessage {
 
    public String orderId;
    public String customerId;
    public List<Item> items;
    public Instant createdAt;
 
    public static class Item {
        public String productId;
        public int quantity;
    }
}
 
