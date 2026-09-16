package stock_service.domain.exception;

public class StockNotFoundException extends RuntimeException {
 
    public StockNotFoundException(String productId) {
        super("No existe stock registrado para el producto: " + productId);
    }
}
