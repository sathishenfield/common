//package com.core.lib.config;
//
//import com.azure.messaging.servicebus.ServiceBusClientBuilder;
//import com.azure.messaging.servicebus.ServiceBusSenderClient;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class AzureServiceBusConfig {
//
//    @Value("${spring.cloud.azure.servicebus.connection-string}")
//    private String connectionString;
//
//    @Value("${spring.cloud.azure.servicebus.queue-name}")
//    private String queueName;
//
//    @Bean
//    public ServiceBusSenderClient senderClient() {
//        return new ServiceBusClientBuilder()
//                .connectionString(connectionString)
//                .sender()
//                .queueName(queueName)
//                .buildClient();
//    }
//}
