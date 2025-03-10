package com.example.Search.repository;

import com.example.Search.entity.Product;
import org.springframework.data.solr.repository.Query;
import org.springframework.data.solr.repository.SolrCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends SolrCrudRepository<Product, String> {
    @Query("productName:*?0*")
    List<Product> findByNameContaining(String productName);
}
