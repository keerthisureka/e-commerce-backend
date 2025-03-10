package com.example.Search.controller;

import com.example.Search.dto.ApiResponse;
import com.example.Search.dto.ProductResponseDto;
import com.example.Search.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<ProductResponseDto>>> searchProducts(@RequestParam String productName) {
        ApiResponse<List<ProductResponseDto>> response = new ApiResponse<>("Products matching your search are listed below: ", HttpStatus.OK, productService.searchProducts(productName));
        return ResponseEntity.ok(response);
    }
}
