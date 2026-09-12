package stock_service.domain.exception;

public class StockAlreadyExistsException extends RuntimeException {
 
    public StockAlreadyExistsException(String productId) {
        super("Ya existe stock registrado para el producto: " + productId);
    }
}