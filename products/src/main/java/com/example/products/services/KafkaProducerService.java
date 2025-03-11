package com.example.products.services;

import com.example.products.dto.ProductKafkaProduceDto;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaProducerService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendProductResponse(String topic, String productKafkaProduceDto) {
        kafkaTemplate.send(topic, productKafkaProduceDto);
    }
}
