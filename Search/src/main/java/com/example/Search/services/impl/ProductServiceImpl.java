package com.example.Search.services.impl;

import com.example.Search.dto.ProductKafkaProduceDto;
import com.example.Search.entity.Product;
import com.example.Search.services.ProductService;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private SolrTemplate solrTemplate;

    @Override
    public List<ProductKafkaProduceDto> searchProducts(String productName) {
        SolrQuery query = new SolrQuery();
        query.setQuery("productName:*" + productName + "*");
        query.addFilterQuery("productMerchantStock:[1 TO *]");

        // Add the custom score function to rank merchants
        String scoreFunction = "sum("
                + "recip(productMerchantPrice,5,20,20),"
                + "recip(totalProductsOfferedByMerchant,1,12,12),"
                + "recip(totalProductsSoldByMerchant,1,8,8),"
                + "recip(productMerchantStock,1,10,10),"
                + "recip(merchantRating,1,5,5),"
                + "recip(productMerchantRating,1,5,5)"
                + ")";

        // Set the function query for Solr to use for sorting
        query.set("fl", "*,score");
        query.set("defType", "edismax");
        query.set("bf", scoreFunction);
        query.setSort("score", SolrQuery.ORDER.desc);

        try {
            QueryResponse response = solrTemplate.getSolrClient().query("mycore", query);
            List<Product> products =  response.getBeans(Product.class);
            List<ProductKafkaProduceDto> productDtoList = new ArrayList<>();
            for (Product product : products) {
                ProductKafkaProduceDto productDto = new ProductKafkaProduceDto();
                BeanUtils.copyProperties(product, productDto);
                productDtoList.add(productDto);
            }
            return productDtoList;
        } catch (SolrServerException | IOException e) {
            System.err.println("Error executing Solr query: " + e.getMessage());
            return Collections.emptyList();
        }
    }
}
