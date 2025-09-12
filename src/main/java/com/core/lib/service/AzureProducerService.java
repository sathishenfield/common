//package com.core.lib.service;
//
//import com.azure.messaging.servicebus.ServiceBusMessage;
//import com.azure.messaging.servicebus.ServiceBusSenderClient;
//import com.core.lib.entity.TaxRecord;
//import com.core.lib.exception.BusinessException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.extern.log4j.Log4j2;
//import org.springframework.stereotype.Service;
//
//import java.time.Duration;
//
//@Service
//@Log4j2
//public class AzureProducerService {
//
//    private final ServiceBusSenderClient senderClient;
//    private final ObjectMapper objectMapper;
//
//    public AzureProducerService(ServiceBusSenderClient senderClient,ObjectMapper objectMapper) {
//        this.senderClient = senderClient;
//        this.objectMapper = objectMapper;
//    }
//
//    public void sendMessage(TaxRecord record) {
//        try {
//            String json = objectMapper.writeValueAsString(record);
//            ServiceBusMessage message = new ServiceBusMessage(json);
//            message.setTimeToLive(Duration.ofHours(1)); // ⬅️ TTL set to 1 hour
//            log.info("Sent message: {}", json);
//        } catch (Exception e) {
//            throw new BusinessException("Failed to send message", e);
//        }
//    }
//}
