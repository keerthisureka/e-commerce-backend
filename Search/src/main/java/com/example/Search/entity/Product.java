package com.example.Search.entity;

import org.apache.solr.client.solrj.beans.Field;
import org.springframework.data.annotation.Id;
import org.springframework.data.solr.core.mapping.SolrDocument;

@SolrDocument(collection = "mycore")
public class Product {
    @Id
    @Field("productId")
    private String productId;

    @Field("productName")
    private String productName;

    @Field("productImageUrl")
    private String productImageUrl;

    @Field("productDescription")
    private String productDescription;

    @Field("productUsp")
    private String productUsp;

    @Field("merchantId")
    private String merchantId;

    @Field("merchantName")
    private String merchantName;

    @Field("merchantPrice")
    private Double merchantPrice;

    @Field("merchantScore")
    private Double merchantScore;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductImageUrl() {
        return productImageUrl;
    }

    public void setProductImageUrl(String productImageUrl) {
        this.productImageUrl = productImageUrl;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getProductUsp() {
        return productUsp;
    }

    public void setProductUsp(String productUsp) {
        this.productUsp = productUsp;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public Double getMerchantPrice() {
        return merchantPrice;
    }

    public void setMerchantPrice(Double merchantPrice) {
        this.merchantPrice = merchantPrice;
    }

    public Double getMerchantScore() {
        return merchantScore;
    }

    public void setMerchantScore(Double merchantScore) {
        this.merchantScore = merchantScore;
    }
}
