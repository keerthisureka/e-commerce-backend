package com.example.Search.repository;

import com.example.Search.entity.Product;
import org.springframework.data.solr.repository.Query;
import org.springframework.data.solr.repository.SolrCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends SolrCrudRepository<Product, String> {
    @Query("productId:?0 AND merchantId:?1")
    Product findByProductIdAndMerchantId(String productId, String merchantId);
}
