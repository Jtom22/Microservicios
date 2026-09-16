package stock_service.infrastructure.adapter.in.web.dto.request;

import jakarta.validation.constraints.Min;

public record ReplenishStockRequest(
 
    @Min(value = 1, message = "La cantidad a reponer debe ser al menos 1")
    int amount
) {}