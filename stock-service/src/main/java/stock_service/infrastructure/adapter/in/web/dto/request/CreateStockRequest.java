package stock_service.infrastructure.adapter.in.web.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
 
public record CreateStockRequest(
 
    @NotBlank(message = "El productId no puede estar vacío")
    String productId,
 
    @Min(value = 0, message = "La cantidad inicial no puede ser negativa")
    int initialQuantity
) {}