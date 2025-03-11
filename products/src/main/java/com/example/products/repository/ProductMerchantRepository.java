package com.example.products.repository;

import com.example.products.entity.ProductMerchant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductMerchantRepository extends JpaRepository<ProductMerchant, String> {
    ProductMerchant findByProductIdAndMerchantId(String id, String merchantId);
}
