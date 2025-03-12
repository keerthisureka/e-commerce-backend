package com.example.Search.controller;

import com.example.Search.dto.ApiResponse;
import com.example.Search.dto.ProductKafkaProduceDto;
import com.example.Search.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Search")
@CrossOrigin(origins = "*")
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping("/getByProductName")
    public ResponseEntity<ApiResponse<List<ProductKafkaProduceDto>>> searchProducts(@RequestParam String productName) {
        ApiResponse<List<ProductKafkaProduceDto>> response = new ApiResponse<>("Products matching your search are listed below: ", HttpStatus.OK, productService.searchProducts(productName));
        return ResponseEntity.ok(response);
    }
}
