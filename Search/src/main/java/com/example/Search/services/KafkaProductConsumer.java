package com.example.Search.services;

import com.example.Search.dto.ProductResponseDto;

public interface KafkaProductConsumer {
    public void consume(ProductResponseDto productResponseDto);
}
