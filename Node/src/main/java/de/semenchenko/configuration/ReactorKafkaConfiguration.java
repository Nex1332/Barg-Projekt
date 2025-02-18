package de.semenchenko.configuration;

import de.semenchenko.service.dto.UpdateMessage;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverOptions;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderOptions;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class ReactorKafkaConfiguration {
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Bean
    public KafkaSender<String, UpdateMessage> kafkaUpdateMessageSender() {
        Map<String, Object> producerProps = new HashMap<>();
        producerProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        SenderOptions<String, UpdateMessage> senderOptions = SenderOptions.create(producerProps);
        return KafkaSender.create(senderOptions);
    }

    @Bean
    public KafkaSender<String, SendMessage> kafkaSendMessageSender() {
        Map<String, Object> producerProps = new HashMap<>();
        producerProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        SenderOptions<String, SendMessage> senderOptions =
                SenderOptions.<String, SendMessage>create(producerProps)
                        .maxInFlight(1024);

        return KafkaSender.create(senderOptions);
    }

    @Bean
    public KafkaReceiver<String, SendMessage> kafkaSendMessageReceiver() {
        Map<String, Object> consumerProps = new HashMap<>();
        consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, "node-group");
        consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);

        consumerProps.put(JsonDeserializer.TRUSTED_PACKAGES, "*");

        ReceiverOptions<String, SendMessage> receiverOptions =
                ReceiverOptions.<String, SendMessage>create(consumerProps)
                        .subscription(Collections.singleton("sendMessage-requests"));

        return KafkaReceiver.create(receiverOptions);
    }

    @Bean
    public KafkaReceiver<String, Update> kafkaUpdateReceiver() {
        Map<String, Object> consumerProps = new HashMap<>();
        consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, "node-group");
        consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);

        consumerProps.put(JsonDeserializer.TRUSTED_PACKAGES, "*");

        ReceiverOptions<String, Update> receiverOptions =
                ReceiverOptions.<String, Update>create(consumerProps)
                        .subscription(Collections.singleton("node-requests"));

        return KafkaReceiver.create(receiverOptions);
    }
}
