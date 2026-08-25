package stock_service.domain.exception;

public class InsufficientStockException extends RuntimeException {
 
    public InsufficientStockException(String productId, int available, int requested) {
        super("Stock insuficiente para el producto " + productId
                + ": disponible=" + available + ", solicitado=" + requested);
    }
}
 
