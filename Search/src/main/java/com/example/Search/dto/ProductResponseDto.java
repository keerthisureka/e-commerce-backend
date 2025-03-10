package com.example.Search.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class ProductResponseDto implements Serializable{
    @JsonProperty("productId")
    private String productId;

    @JsonProperty("productName")
    private String productName;

    @JsonProperty("productImageUrl")
    private String productImageUrl;

    @JsonProperty("productDescription")
    private String productDescription;

    @JsonProperty("productUsp")
    private String productUsp;

    @JsonProperty("merchantId")
    private String merchantId;

    @JsonProperty("merchantName")
    private String merchantName;

    @JsonProperty("merchantPrice")
    private Double merchantPrice;

    @JsonProperty("merchantScore")
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
