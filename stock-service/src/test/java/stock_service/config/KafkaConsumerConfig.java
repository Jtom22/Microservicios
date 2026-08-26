package stock_service.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.serializer.JacksonJsonDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverOptions;
import stock_service.infrastructure.adapter.in.messaging.OrderCreatedMessage;
import tools.jackson.databind.annotation.JsonDeserialize;

@Configuration
public class KafkaConsumerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${app.kafka.topic.order-created}")
    private String orderCreatedTopic;

    @Value("${app.kafka.consumer-group}")
    private String consumerGroup;

    @Bean
    public KafkaReceiver<String, OrderCreatedMessage> kafkaReceiver() {
        // 1. Configurar el deserializador directamente con sus opciones
        JacksonJsonDeserializer<OrderCreatedMessage> deserializer = new JacksonJsonDeserializer<>(
                OrderCreatedMessage.class);
        deserializer.addTrustedPackages("stock_service.infrastructure.adapter.in.messaging");
        // Propiedades de conexion
        Map<String, Object> props = new HashMap<>();

        //Define la dirección y puerto del clúster de Kafka
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        //Assigna un identificador de grupo a este consumidor
        props.put(ConsumerConfig.GROUP_ID_CONFIG, consumerGroup);
        //Cómo transformar la clave (key) del mensaje a un objeto Java(String en este caso)
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        // Estrategia de lectura cuando el consumidor se conecta por primera vez(earliest o latest)
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        // Confirmamos el offset manualmente en el consumer desactivando el guardado
        // (record.receiverOffset().acknowledge()),
        // no automáticamente, para no perder mensajes si el proceso se cae a mitad de
        // un lote.
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        
        // 3. Pasar el deserializador explícitamente a ReceiverOptions
        ReceiverOptions<String, OrderCreatedMessage> options = ReceiverOptions
                .<String, OrderCreatedMessage>create(props)
                .withValueDeserializer(deserializer)
                .subscription(java.util.Collections.singleton(orderCreatedTopic));

        return KafkaReceiver.create(options);
    }
}
