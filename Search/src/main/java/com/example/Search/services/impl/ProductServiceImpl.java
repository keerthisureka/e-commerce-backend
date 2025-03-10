package com.example.Search.services.impl;

import com.example.Search.dto.ProductResponseDto;
import com.example.Search.entity.Product;
import com.example.Search.repository.ProductRepository;
import com.example.Search.services.ProductService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<ProductResponseDto> searchProducts(String productName) {
        List<Product> products = productRepository.findByNameContaining(productName);

        List<ProductResponseDto> productDtoList = new ArrayList<>();
        for (Product product : products) {
            ProductResponseDto productDto = new ProductResponseDto();
            BeanUtils.copyProperties(product, productDto);
            productDtoList.add(productDto);
        }
        return productDtoList;
    }
}
