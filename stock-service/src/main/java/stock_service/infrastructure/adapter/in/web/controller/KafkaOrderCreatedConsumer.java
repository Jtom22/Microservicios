package stock_service.infrastructure.adapter.in.web.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import reactor.core.publisher.Mono;
import reactor.kafka.receiver.KafkaReceiver;
import stock_service.infrastructure.adapter.in.messaging.dto.OrderCreatedMessage;
import stock_service.domain.model.event.OrderCreatedEvent;
import stock_service.domain.port.in.StockUseCase;

/**
 * StockUseCase
 */
@Component
public class KafkaOrderCreatedConsumer {

    private final KafkaReceiver<String, OrderCreatedMessage> kafkaReceiver;
    private final StockUseCase stockUseCase;

    public KafkaOrderCreatedConsumer(
            KafkaReceiver<String, OrderCreatedMessage> kafkaReceiver,
            StockUseCase stockUseCase) {
        this.kafkaReceiver = kafkaReceiver;
        this.stockUseCase = stockUseCase;
    }

    @PostConstruct
    public void startConsuming() {
        kafkaReceiver.receive()
                .concatMap(record -> {
                    OrderCreatedEvent event = toDomainEvent(record.value());
                    return stockUseCase.handle(event)
                            // Si falla el descuento de stock, igual confirmamos el
                            // offset para no reprocesar en loop infinito. Para un PoC
                            // esto es aceptable; en producción iría a un dead-letter topic.
                            .doOnError(err -> System.err.println(
                                    "Error procesando OrderCreatedEvent " + event.orderId() + ": " + err.getMessage()))
                            .onErrorResume(err -> Mono.empty())
                            .doFinally(signal -> record.receiverOffset().acknowledge());
                })
                .subscribe();
    }

    private OrderCreatedEvent toDomainEvent(OrderCreatedMessage message) {
        List<OrderCreatedEvent.OrderItem> items = message.items.stream()
                .map(i -> new OrderCreatedEvent.OrderItem(i.productId, i.quantity))
                .collect(Collectors.toList());

        return new OrderCreatedEvent(message.orderId, message.customerId, items, message.createdAt);
    }
}
