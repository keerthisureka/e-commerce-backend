package com.example.products.controller;

import java.util.List;
import com.example.products.dto.ApiResponse;
import com.example.products.dto.ProductCardResponseDto;
import com.example.products.dto.ProductRequestDto;
import com.example.products.dto.ProductResponseDto;
import com.example.products.services.Impl.ProductServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/Products")
@CrossOrigin(origins = "*")
public class ProductController {

    @Autowired
    private ProductServiceImpl productService;

    @PostMapping("/addProduct")
    public ResponseEntity<ApiResponse<Boolean>> addProduct(@RequestBody List<ProductRequestDto> productRequestDto) {
        return ResponseEntity.ok(productService.addProduct(productRequestDto));
    }

    @GetMapping("/getAllProducts")
    public ResponseEntity<ApiResponse<List<ProductCardResponseDto>>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/getByProductId/{productId}")
    public ResponseEntity<ApiResponse<ProductResponseDto>> getByProductId(@PathVariable String productId) {
        return ResponseEntity.ok(productService.getByProductId(productId));
    }

    @GetMapping("/getProductMerchantId/{productId}/{merchantId}")
    public ResponseEntity<ApiResponse<String>> getProductMerchantId(@PathVariable String productId, @PathVariable String merchantId) {
        System.out.println(productId + " " + merchantId);
        return ResponseEntity.ok(productService.getProductMerchantId(productId,merchantId));
    }
}
