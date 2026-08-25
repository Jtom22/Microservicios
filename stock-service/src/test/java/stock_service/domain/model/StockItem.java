package stock_service.domain.model;

import stock_service.domain.exception.InsufficientStockException;

/**
 * Entidad de dominio pura: representa el stock disponible de un producto.
 * Sin @Document, sin @Id de Spring Data — esas anotaciones son detalles de
 * CÓMO se persiste, no de QUÉ es un StockItem. Viven en el adapter de Mongo.
 */
public class StockItem {
 
    private final String productId;
    private int quantity;
 
    public StockItem(String productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }
 
    /**
     * Regla de negocio central de este microservicio: no se puede descontar
     * más stock del disponible. Poner en el dominio, no en el adapter de
     * Mongodb ni en el consumer de Kafka — es una regla de negocio, no un
     * detalle técnico, así que tiene que sobrevivir a cualquier cambio de
     * infraestructura.
     */
    public void decrease(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("La cantidad a descontar debe ser positiva");
        }
        if (amount > quantity) {
            throw new InsufficientStockException(productId, quantity, amount);
        }
        this.quantity -= amount;
    }
 
    public String getProductId() {
        return productId;
    }
 
    public int getQuantity() {
        return quantity;
    }
}
 