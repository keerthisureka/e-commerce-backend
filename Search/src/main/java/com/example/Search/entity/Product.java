package com.example.Search.entity;

import org.apache.solr.client.solrj.beans.Field;
import org.springframework.data.annotation.Id;
import org.springframework.data.solr.core.mapping.SolrDocument;

@SolrDocument(collection = "mycore")
public class Product {
    @Id
    @Field("id")
    private String id;

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

    @Field("productMerchantPrice")
    private Double productMerchantPrice;

    @Field("totalProductsOfferedByMerchant")
    private Long totalProductsOfferedByMerchant;

    @Field("totalProductsSoldByMerchant")
    private Long totalProductsSoldByMerchant;

    @Field("productMerchantStock")
    private Long productMerchantStock;

    @Field("merchantRating")
    private Double merchantRating;

    @Field("productMerchantRating")
    private Double productMerchantRating;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public Double getProductMerchantPrice() {
        return productMerchantPrice;
    }

    public void setProductMerchantPrice(Double productMerchantPrice) {
        this.productMerchantPrice = productMerchantPrice;
    }

    public Long getTotalProductsOfferedByMerchant() {
        return totalProductsOfferedByMerchant;
    }

    public void setTotalProductsOfferedByMerchant(Long totalProductsOfferedByMerchant) {
        this.totalProductsOfferedByMerchant = totalProductsOfferedByMerchant;
    }

    public Long getTotalProductsSoldByMerchant() {
        return totalProductsSoldByMerchant;
    }

    public void setTotalProductsSoldByMerchant(Long totalProductsSoldByMerchant) {
        this.totalProductsSoldByMerchant = totalProductsSoldByMerchant;
    }

    public Long getProductMerchantStock() {
        return productMerchantStock;
    }

    public void setProductMerchantStock(Long productMerchantStock) {
        this.productMerchantStock = productMerchantStock;
    }

    public Double getMerchantRating() {
        return merchantRating;
    }

    public void setMerchantRating(Double merchantRating) {
        this.merchantRating = merchantRating;
    }

    public Double getProductMerchantRating() {
        return productMerchantRating;
    }

    public void setProductMerchantRating(Double productMerchantRating) {
        this.productMerchantRating = productMerchantRating;
    }
}
