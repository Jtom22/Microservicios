package stock_service.infrastructure.adapter.in.web.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import stock_service.domain.port.in.StockUseCase;
import stock_service.infrastructure.adapter.in.web.dto.request.CreateStockRequest;
import stock_service.infrastructure.adapter.in.web.dto.request.ReplenishStockRequest;
import stock_service.infrastructure.adapter.in.web.dto.response.StockMovementResponse;
import stock_service.infrastructure.adapter.in.web.dto.response.StockResponse;
 
/**
 * Segundo adapter de ENTRADA del microservicio (el primero es
 * KafkaOrderCreatedConsumer). Ambos le hablan a los mismos casos de uso del
 * dominio a través de sus puertos — este controller no sabe nada de Kafka,
 * ni el consumer de Kafka sabe nada de HTTP.
 */

@RestController
@RequestMapping("/api/stock")
public class StockController {

    private final StockUseCase stockUseCase;

    public StockController(StockUseCase stockUseCase) {
        this.stockUseCase = stockUseCase;
    }

    @PostMapping
    public Mono<ResponseEntity<StockResponse>> create(@Valid @RequestBody CreateStockRequest request) {
        return stockUseCase.create(request.productId(), request.initialQuantity())
                .map(StockResponse::from)
                .map(response -> ResponseEntity.status(HttpStatus.CREATED).body(response));
    }

    @PatchMapping("/{productId}/replenish")
    public Mono<StockResponse> replenish(
            @PathVariable String productId,
            @Valid @RequestBody ReplenishStockRequest request) {
        return stockUseCase.replenish(productId, request.amount())
                .map(StockResponse::from);
    }

    @GetMapping("/{productId}")
    public Mono<StockResponse> getByProductId(@PathVariable String productId) {
        return stockUseCase.getByProductId(productId)
                .map(StockResponse::from);
    }

    @GetMapping
    public Flux<StockResponse> getAll() {
        return stockUseCase.getAll()
                .map(StockResponse::from);
    }

    @GetMapping("/{productId}/movements")
    public Flux<StockMovementResponse> getMovements(@PathVariable String productId) {
        return stockUseCase.getMovements(productId)
                .map(StockMovementResponse::from);
    }
}