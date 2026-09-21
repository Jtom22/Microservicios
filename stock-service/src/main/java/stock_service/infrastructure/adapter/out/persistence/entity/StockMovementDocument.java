package stock_service.infrastructure.adapter.out.persistence.entity;

import java.time.Instant;
 
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
 
@Document(collection = "stock_movements")
public class StockMovementDocument {
 
    @Id
    private String id;
    private String productId;
    private String type;
    private int amount;
    private int resultingQuantity;
    private String reason;
    private Instant occurredAt;
 
    public StockMovementDocument() {
    }
 
    public StockMovementDocument(String id, String productId, String type, int amount,
                                  int resultingQuantity, String reason, Instant occurredAt) {
        this.id = id;
        this.productId = productId;
        this.type = type;
        this.amount = amount;
        this.resultingQuantity = resultingQuantity;
        this.reason = reason;
        this.occurredAt = occurredAt;
    }
 
    public String getId() {
        return id;
    }
 
    public void setId(String id) {
        this.id = id;
    }
 
    public String getProductId() {
        return productId;
    }
 
    public void setProductId(String productId) {
        this.productId = productId;
    }
 
    public String getType() {
        return type;
    }
 
    public void setType(String type) {
        this.type = type;
    }
 
    public int getAmount() {
        return amount;
    }
 
    public void setAmount(int amount) {
        this.amount = amount;
    }
 
    public int getResultingQuantity() {
        return resultingQuantity;
    }
 
    public void setResultingQuantity(int resultingQuantity) {
        this.resultingQuantity = resultingQuantity;
    }
 
    public String getReason() {
        return reason;
    }
 
    public void setReason(String reason) {
        this.reason = reason;
    }
 
    public Instant getOccurredAt() {
        return occurredAt;
    }
 
    public void setOccurredAt(Instant occurredAt) {
        this.occurredAt = occurredAt;
    }
}
