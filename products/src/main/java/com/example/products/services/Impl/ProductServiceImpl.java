package com.example.products.services.Impl;

import com.example.products.dto.*;
import com.example.products.entity.Merchant;
import com.example.products.entity.Product;
import com.example.products.entity.ProductMerchant;
import com.example.products.repository.MerchantRepository;
import com.example.products.repository.ProductMerchantRepository;
import com.example.products.repository.ProductRepository;
import com.example.products.services.KafkaProducerService;
import com.example.products.services.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductMerchantRepository productMerchantRepository;

    @Autowired
    private MerchantRepository merchantRepository;

    @Autowired
    private KafkaProducerService kafkaProducerService;


    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public ApiResponse<Boolean> addProduct(ProductRequestDto productRequestDto) {
        Product product = new Product();
        product.setName(productRequestDto.getName());
        product.setImageUrl(productRequestDto.getImageUrl());
        product.setDescription(productRequestDto.getDescription());
        product.setUsp(productRequestDto.getUsp());
        productRepository.save(product);
        List<ProductMerchantRequestDto> productMerchantRequestDtoList = productRequestDto.getProductMerchantRequestDtoList();
        List<ProductMerchantResponseDto> productMerchantResponseDtoList = new ArrayList<>();
        for(ProductMerchantRequestDto productMerchantRequestDto: productMerchantRequestDtoList) {
            ProductMerchant productMerchant = new ProductMerchant();
            productMerchant.setMerchantId(productMerchantRequestDto.getMerchantId());
            productMerchant.setPrice(productMerchantRequestDto.getPrice());
            productMerchant.setStock(productMerchantRequestDto.getStock());
            productMerchant.setRatings(productMerchantRequestDto.getRatings());
            productMerchant.setProductId(product.getId());

            Merchant merchant = merchantRepository.findById(productMerchantRequestDto.getMerchantId()).get();
            merchant.setTotalProductsListedByMerchant(merchant.getTotalProductsListedByMerchant() + productMerchantRequestDto.getStock());
            merchantRepository.save(merchant);

            ProductMerchantResponseDto productMerchantResponseDto = new ProductMerchantResponseDto();
            productMerchantResponseDto.setMerchantId(productMerchantRequestDto.getMerchantId());
            productMerchantResponseDto.setMerchantName(merchantRepository.findById(productMerchantRequestDto.getMerchantId()).get().getName());
            productMerchantResponseDto.setPrice(productMerchantRequestDto.getPrice());
            productMerchantResponseDtoList.add(productMerchantResponseDto);

            productMerchantRepository.save(productMerchant);
        }
        Collections.sort(productMerchantResponseDtoList, new ScoreComparator());
        product.setMerchantList(productMerchantResponseDtoList);

        publishToKafkaTopic(productRepository.save(product));

        return new ApiResponse<>(HttpStatus.CREATED," Product added", true);
    }

    @Override
    public ApiResponse<List<ProductCardResponseDto>> getAllProducts() {
        List<Product> allProducts = productRepository.findAll();
        List<ProductCardResponseDto> allProductsResponse = new ArrayList<>();

        for(Product product: allProducts) {
            ProductCardResponseDto productCardResponseDto  = new ProductCardResponseDto();
            productCardResponseDto.setProductId(product.getId());
            productCardResponseDto.setName(product.getName());
            productCardResponseDto.setPrice(Objects.requireNonNull(product.getMerchantList().get(0)).getPrice());
            productCardResponseDto.setDescription(product.getDescription());
            productCardResponseDto.setImage(product.getImageUrl());
            productCardResponseDto.setUsp(product.getUsp());
            allProductsResponse.add(productCardResponseDto);
        }

        return new ApiResponse<>(HttpStatus.OK, "fetched all the products", allProductsResponse);
    }

    @Override
    public ApiResponse<ProductResponseDto> getByProductId(String productId) {
        Product product = productRepository.findById(productId).get();
        ProductResponseDto productResponseDto = new ProductResponseDto();
        productResponseDto.setId(product.getId());
        productResponseDto.setName(product.getName());
        productResponseDto.setDescription(product.getDescription());
        productResponseDto.setImageUrl(product.getImageUrl());
        productResponseDto.setUsp(product.getUsp());
        productResponseDto.setMerchantList(product.getMerchantList());

        return new ApiResponse<>(HttpStatus.FOUND, "Fetched The product", productResponseDto);
    }

    private void  publishToKafkaTopic(Product product) {
        for(ProductMerchantResponseDto productMerchantResponseDto : product.getMerchantList()) {
            ProductKafkaProduceDto productKafkaProduceDto = new ProductKafkaProduceDto();
            productKafkaProduceDto.setProductId(product.getId());
            productKafkaProduceDto.setProductName(product.getName());
            productKafkaProduceDto.setProductImageUrl(product.getImageUrl());
            productKafkaProduceDto.setProductDescription(product.getDescription());
            productKafkaProduceDto.setProductUsp(product.getUsp());
            productKafkaProduceDto.setMerchantId(productMerchantResponseDto.getMerchantId());
            productKafkaProduceDto.setMerchantName(productMerchantResponseDto.getMerchantName());
            productKafkaProduceDto.setProductMerchantPrice(productMerchantResponseDto.getPrice());
            productKafkaProduceDto.setTotalProductsOfferedByMerchant(merchantRepository.findById(productMerchantResponseDto.getMerchantId()).get().getTotalProductsListedByMerchant());
            productKafkaProduceDto.setTotalProductsSoldByMerchant(merchantRepository.findById(productMerchantResponseDto.getMerchantId()).get().getTotalProductsSoldByMerchant());
            productKafkaProduceDto.setProductMerchantStock(productMerchantRepository.findByProductIdAndMerchantId(product.getId(),productMerchantResponseDto.getMerchantId()).getStock());
            productKafkaProduceDto.setMerchantRating(merchantRepository.findById(productMerchantResponseDto.getMerchantId()).get().getRatings());
            productKafkaProduceDto.setProductMerchantRating(productMerchantRepository.findByProductIdAndMerchantId(product.getId(),productMerchantResponseDto.getMerchantId()).getRatings());

            try {
                kafkaProducerService.sendProductResponse("product-topic", objectMapper.writeValueAsString(productKafkaProduceDto));
                ;
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

}

class ScoreComparator implements Comparator<ProductMerchantResponseDto> {

    @Override
    public int compare(ProductMerchantResponseDto o1, ProductMerchantResponseDto o2) {
        if(o1.getScore() < o2.getScore()) {
            return 1;
        } else if (o1.getScore() > o2.getScore()){
            return -1;
        }
        return 0;
    }
}
