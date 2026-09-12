package stock_service.domain.model;

import java.time.Instant;

public class StockMovement {
 
    private final String id;
    private final String productId;
    private final MovementType type;
    private final int amount;
    private final int resultingQuantity;
    private final String reason;
    private final Instant occurredAt;
 
    // Constructor completo: para reconstruir un StockMovement que YA existía
    // (por ejemplo, al leerlo de vuelta desde Mongo).
    public StockMovement(String id, String productId, MovementType type, int amount,
                          int resultingQuantity, String reason, Instant occurredAt) {
        this.id = id;
        this.productId = productId;
        this.type = type;
        this.amount = amount;
        this.resultingQuantity = resultingQuantity;
        this.reason = reason;
        this.occurredAt = occurredAt;
    }
 
    /**
     * Factory para crear un movimiento NUEVO (todavía sin id, porque Mongo se
     * lo asigna al guardar; con occurredAt = ahora mismo).
     */
    public static StockMovement newMovement(String productId, MovementType type, int amount,
                                             int resultingQuantity, String reason) {
        return new StockMovement(null, productId, type, amount, resultingQuantity, reason, Instant.now());
    }
 
    public String getId() {
        return id;
    }
 
    public String getProductId() {
        return productId;
    }
 
    public MovementType getType() {
        return type;
    }
 
    public int getAmount() {
        return amount;
    }
 
    public int getResultingQuantity() {
        return resultingQuantity;
    }
 
    public String getReason() {
        return reason;
    }
 
    public Instant getOccurredAt() {
        return occurredAt;
    }
}