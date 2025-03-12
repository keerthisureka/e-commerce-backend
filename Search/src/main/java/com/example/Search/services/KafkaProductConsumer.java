package com.example.Search.services;

import com.example.Search.dto.ProductKafkaProduceDto;

public interface KafkaProductConsumer {
    public void consume(String productResponseDto);
}
