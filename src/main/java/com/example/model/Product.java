package com.example.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Product {
    private String id;
    private String title;
    private String description;
    private String category;
    private transient int price;
    private List<String> categories;
    private PriceInfo priceInfo;

    // Default constructor for JSON deserialization
    public Product() {
    }

    public Product(String id, String title, String description, String category, int price) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.category = category;
        this.price = price;
    }

    public Product(String id, String title, String description, List<String> categories, int price) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.categories = categories;
        this.price = price;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getPrice() {
        if (priceInfo != null) {
            return (int) priceInfo.price;
        }
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public PriceInfo getPriceInfo() {
        return priceInfo;
    }

    public void setPriceInfo(PriceInfo priceInfo) {
        this.priceInfo = priceInfo;
    }

    public static class PriceInfo {
        public double price;
        public double cost;
        public double originalPrice;
        public String currencyCode;
    }
} 