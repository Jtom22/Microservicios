package stock_service.infrastructure.adapter.in.messaging.consumer;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Component;
 
import jakarta.annotation.PostConstruct;
import reactor.core.publisher.Mono;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverRecord;
import reactor.kafka.sender.SenderRecord;
import stock_service.domain.model.event.OrderCreatedEvent;
import stock_service.domain.port.in.StockUseCase;
import stock_service.infrastructure.adapter.in.messaging.dto.OrderCreatedMessage;

//  Único punto del sistema que sabe que "los pedidos llegan por Kafka".
//  Traduce OrderCreatedMessage (formato de transporte) a OrderCreatedEvent
//  (concepto de dominio) antes de llamar al caso de uso.

@Component
public class KafkaOrderCreatedConsumer {
 
    private final KafkaReceiver<String, OrderCreatedMessage> kafkaReceiver;
    private final StockUseCase decreaseStockUseCase;
 
    public KafkaOrderCreatedConsumer(
            KafkaReceiver<String, OrderCreatedMessage> kafkaReceiver,
            StockUseCase decreaseStockUseCase) {
        this.kafkaReceiver = kafkaReceiver;
        this.decreaseStockUseCase = decreaseStockUseCase;
    }
 
    //Utilizamos el service despues de adaptar el dto a uno dentro del dominio que entienda
    @PostConstruct
    public void startConsuming() {
        kafkaReceiver.receive()
                .concatMap(record -> {
                    OrderCreatedEvent event = toDomainEvent(record.value());
                    return decreaseStockUseCase.handle(event)
                            // Si falla el descuento de stock,aun asi confirmamos el
                            // offset para no reprocesar en loop infinito. Para un concepto
                            // esto es adecuado; en producción iría a un dead-letter topic.->hacer a futuro
                            .doOnError(err -> System.err.println(
                                    "Error procesando OrderCreatedEvent " + event.orderId() + ": " + err.getMessage()))
                            .onErrorResume(err -> Mono.empty())
                            // .onErrorResume(err -> sendToDeadLetterTopic(record,err))
                            
                            .doFinally(signal -> record.receiverOffset().acknowledge());
                })
                .subscribe();
    }
    //Mapper entre el core y el exterior
 
    private OrderCreatedEvent toDomainEvent(OrderCreatedMessage message) {
        List<OrderCreatedEvent.OrderItem> items = message.items.stream()
                .map(i -> new OrderCreatedEvent.OrderItem(i.productId, i.quantity))
                .collect(Collectors.toList());
 
        return new OrderCreatedEvent(message.orderId, message.customerId, items, message.createdAt);
    }


// private Mono<Void> sendToDeadLetterTopic(ReceiverRecord<String, String> record, Throwable err) {
//     // Creamos el registro a enviar al tópico DLT
//     ProducerRecord<String, String> dltRecord = new ProducerRecord<>(
//             "orders.created.DLT", 
//             record.key(), 
//             record.value()
//     );
    
//     // Añadimos headers informativos sobre el error para facilitar el debug
//     dltRecord.headers()
//             .add("x-original-topic", record.topic().getBytes(StandardCharsets.UTF_8))
//             .add("x-exception-message", err.getMessage().getBytes(StandardCharsets.UTF_8));

//     // Publicamos en Kafka de forma reactiva
//     return kafkaSender.send(Mono.just(SenderRecord.create(dltRecord, null)))
//             .then(); // Retorna Mono<Void> al terminar la publicación


}
