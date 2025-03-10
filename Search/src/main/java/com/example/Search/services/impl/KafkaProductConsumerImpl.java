package com.example.Search.services.impl;

import com.example.Search.dto.ProductResponseDto;
import com.example.Search.entity.Product;
import com.example.Search.repository.ProductRepository;
import com.example.Search.services.KafkaProductConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaProductConsumerImpl implements KafkaProductConsumer {
    @Autowired
    private ProductRepository productRepository;

    @Override
    @KafkaListener(topics = "product-topic", groupId = "product-consumer-group")
    public void consume(ProductResponseDto productResponseDto) {
        try {
            Product product = new Product();
            product.setProductId(productResponseDto.getProductId());
            product.setProductName(productResponseDto.getProductName());
            product.setProductImageUrl(productResponseDto.getProductImageUrl());
            product.setProductDescription(productResponseDto.getProductDescription());
            product.setProductUsp(productResponseDto.getProductUsp());
            product.setMerchantId(productResponseDto.getMerchantId());
            product.setMerchantName(productResponseDto.getMerchantName());
            product.setMerchantPrice(productResponseDto.getMerchantPrice());
            product.setMerchantScore(productResponseDto.getMerchantScore());

            productRepository.save(product);
            System.out.println("Product saved to Solr: " + product);
        } catch (Exception e) {
            System.err.println("Error processing Kafka message: " + e.getMessage());
        }
    }
}
