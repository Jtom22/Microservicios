package stock_service.infrastructure.adapter.out.persistence.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

// Este documento es DISTINTO de domain.model.StockItem, a propósito.
// Es el "molde" de cómo se guarda en Mongo (con @Id, @Document). Convertir
// entre este documento y el StockItem de dominio es trabajo de
// MongoStockRepositoryAdapter — así, si mañana cambia el esquema de Mongo
// (o migras a otra base), el dominio ni se entera.
// Entidad 
@Document(collection = "stock")
public class StockDocument {
 
    @Id
    private String productId;
    private int quantity;
 
    public StockDocument() {
    }
 
    public StockDocument(String productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }
 
    public String getProductId() {
        return productId;
    }
 
    public void setProductId(String productId) {
        this.productId = productId;
    }
 
    public int getQuantity() {
        return quantity;
    }
 
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}

