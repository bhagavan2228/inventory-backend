package com.inventory.inventory_management_system.dto;

public class ProductResponse {

    private Long id;
    private String name;
    private double price;
    private String categoryName;

    public ProductResponse(Long id, String name, double price, String categoryName) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.categoryName = categoryName;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getCategoryName() {
        return categoryName;
    }
}
