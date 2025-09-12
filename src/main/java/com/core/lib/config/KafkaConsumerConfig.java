package com.core.lib.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.Map;

/**
 * KafkaConsumerConfig sets up Kafka consumers for the application.
 * <p>
 * It provides a generic consumer for JSON/Object/Array payloads. The configuration
 * includes concurrency settings and error handling.
 * <p>
 * The consumer factory uses JsonDeserializer to handle multiple payload types and
 * StringDeserializer for keys.
 */
@Configuration
@EnableKafka
public class KafkaConsumerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String serverAddress;

    @Value("${spring.kafka.consumer.group-id}")
    private String groupId;

    /**
     * Creates a ConsumerFactory capable of consuming JSON/Object/Array payloads.
     * <p>
     * The JsonDeserializer is configured to trust all packages ("*"). For production,
     * it is safer to restrict this to your specific model packages.
     *
     * @return ConsumerFactory<String, Object> configured with StringDeserializer for keys
     *         and JsonDeserializer for values.
     */
    @Bean
    public ConsumerFactory<String, Object> consumerFactory() {
        JsonDeserializer<Object> deserializer = new JsonDeserializer<>(Object.class);
        deserializer.addTrustedPackages("*");

        return new DefaultKafkaConsumerFactory<>(
                Map.of(
                        ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, serverAddress,
                        ConsumerConfig.GROUP_ID_CONFIG, groupId,
                        ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class,
                        ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class
                ),
                new StringDeserializer(),
                deserializer
        );
    }

    /**
     * Creates a ConcurrentKafkaListenerContainerFactory for consuming messages.
     * <p>
     * This factory supports concurrent consumers, error handling via DefaultErrorHandler,
     * and can be referenced in @KafkaListener annotations.
     *
     * @return ConcurrentKafkaListenerContainerFactory<String, Object> for generic payload consumption.
     */
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Object> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Object> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.setConcurrency(3);
        factory.setCommonErrorHandler(new DefaultErrorHandler());
        return factory;
    }
}