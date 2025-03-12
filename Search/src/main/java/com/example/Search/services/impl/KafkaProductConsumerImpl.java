package com.example.Search.services.impl;

import com.example.Search.dto.ProductKafkaProduceDto;
import com.example.Search.entity.Product;
import com.example.Search.repository.ProductRepository;
import com.example.Search.services.KafkaProductConsumer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaProductConsumerImpl implements KafkaProductConsumer {
    @Autowired
    private ProductRepository productRepository;

    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    @KafkaListener(topics = "product-topic", groupId = "product-consumer-group")
    public void consume(String productResponseDtoString) {
        ProductKafkaProduceDto productKafkaProduceDto = null;
        try {
             productKafkaProduceDto = objectMapper.readValue(productResponseDtoString, ProductKafkaProduceDto.class);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        try {
            Product existingProduct = productRepository.findByProductIdAndMerchantId(productKafkaProduceDto.getProductId(), productKafkaProduceDto.getMerchantId());

            if (existingProduct == null) {
                existingProduct = new Product();
            }
            existingProduct.setProductId(productKafkaProduceDto.getProductId());
            existingProduct.setProductName(productKafkaProduceDto.getProductName());
            existingProduct.setProductImageUrl(productKafkaProduceDto.getProductImageUrl());
            existingProduct.setProductDescription(productKafkaProduceDto.getProductDescription());
            existingProduct.setProductUsp(productKafkaProduceDto.getProductUsp());
            existingProduct.setMerchantId(productKafkaProduceDto.getMerchantId());
            existingProduct.setMerchantName(productKafkaProduceDto.getMerchantName());
            existingProduct.setProductMerchantPrice(productKafkaProduceDto.getProductMerchantPrice());
            existingProduct.setTotalProductsOfferedByMerchant(productKafkaProduceDto.getTotalProductsOfferedByMerchant());
            existingProduct.setTotalProductsSoldByMerchant(productKafkaProduceDto.getTotalProductsSoldByMerchant());
            existingProduct.setProductMerchantStock(productKafkaProduceDto.getProductMerchantStock());
            existingProduct.setMerchantRating(productKafkaProduceDto.getMerchantRating());
            existingProduct.setProductMerchantRating(productKafkaProduceDto.getProductMerchantRating());
            productRepository.save(existingProduct);
            System.out.println("Product saved to Solr: " + existingProduct);
        } catch (Exception e) {
            System.err.println("Error processing Kafka message: " + e.getMessage());
        }
    }
}
