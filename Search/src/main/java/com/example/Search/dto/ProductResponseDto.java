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

    @JsonProperty("productMerchantPrice")
    private Double productMerchantPrice;

    @JsonProperty("totalProductsOfferedByMerchant")
    private Long totalProductsOfferedByMerchant;

    @JsonProperty("totalProductsSoldByMerchant")
    private Long totalProductsSoldByMerchant;

    @JsonProperty("productMerchantStock")
    private Long productMerchantStock;

    @JsonProperty("merchantRating")
    private Double merchantRating;

    @JsonProperty("productMerchantRating")
    private Double productMerchantRating;

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
