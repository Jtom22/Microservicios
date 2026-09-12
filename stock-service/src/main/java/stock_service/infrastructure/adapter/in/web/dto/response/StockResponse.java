package stock_service.infrastructure.adapter.in.web.dto.response;

import stock_service.domain.model.StockItem;

public record StockResponse(
    String productId,
    int quantity
) {
    public static StockResponse from(StockItem stockItem) {
        return new StockResponse(stockItem.getProductId(), stockItem.getQuantity());
    }
}
