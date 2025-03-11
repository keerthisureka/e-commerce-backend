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
            Product existingProduct = productRepository.findByProductIdAndMerchantId(productResponseDto.getProductId(), productResponseDto.getMerchantId());

            if (existingProduct == null) {
                existingProduct = new Product();
            }
            existingProduct.setProductId(productResponseDto.getProductId());
            existingProduct.setProductName(productResponseDto.getProductName());
            existingProduct.setProductImageUrl(productResponseDto.getProductImageUrl());
            existingProduct.setProductDescription(productResponseDto.getProductDescription());
            existingProduct.setProductUsp(productResponseDto.getProductUsp());
            existingProduct.setMerchantId(productResponseDto.getMerchantId());
            existingProduct.setMerchantName(productResponseDto.getMerchantName());
            existingProduct.setProductMerchantPrice(productResponseDto.getProductMerchantPrice());
            existingProduct.setTotalProductsOfferedByMerchant(productResponseDto.getTotalProductsOfferedByMerchant());
            existingProduct.setTotalProductsSoldByMerchant(productResponseDto.getTotalProductsSoldByMerchant());
            existingProduct.setProductMerchantStock(productResponseDto.getProductMerchantStock());
            existingProduct.setMerchantRating(productResponseDto.getMerchantRating());
            existingProduct.setProductMerchantRating(productResponseDto.getProductMerchantRating());
            productRepository.save(existingProduct);
            System.out.println("Product saved to Solr: " + existingProduct);
        } catch (Exception e) {
            System.err.println("Error processing Kafka message: " + e.getMessage());
        }
    }
}
