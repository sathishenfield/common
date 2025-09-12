package com.core.lib.config;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

/**
 * KafkaProducerConfig sets up Kafka producers for the application.
 * <p>
 * It provides two types of KafkaTemplates:
 * <ul>
 *     <li>objectKafkaTemplate: For sending JSON objects, arrays, or generic objects.</li>
 *     <li>stringKafkaTemplate: For sending plain string messages.</li>
 * </ul>
 * <p>
 * Each KafkaTemplate uses a corresponding ProducerFactory which handles serialization
 * of the message key and value.
 */
@Configuration
public class KafkaProducerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String serverAddress;

    /**
     * Creates a ProducerFactory for sending JSON/Object/Array payloads.
     *
     * @return ProducerFactory<String, Object> configured with String key serializer
     *         and JsonSerializer for the value.
     */
    @Bean
    public ProducerFactory<String, Object> objectProducerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, serverAddress);
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(config);
    }

    /**
     * KafkaTemplate for sending JSON/Object/Array messages to Kafka topics.
     *
     * @return KafkaTemplate<String, Object>
     */
    @Bean
    public KafkaTemplate<String, Object> objectKafkaTemplate() {
        return new KafkaTemplate<>(objectProducerFactory());
    }
}
