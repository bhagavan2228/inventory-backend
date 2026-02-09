package com.inventory.inventory_management_system.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class StockMovement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    // +10 for stock-in, -5 for stock-out
    private int quantityChanged;

    // IN or OUT
    private String type;

    // INITIAL_STOCK, STOCK_IN, SALE
    private String reason;

    // When the stock change happened
    private LocalDateTime timestamp;

    public StockMovement() {
    }

    public StockMovement(Product product, int quantityChanged, String type, String reason) {
        this.product = product;
        this.quantityChanged = quantityChanged;
        this.type = type;
        this.reason = reason;
        this.timestamp = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantityChanged() {
        return quantityChanged;
    }

    public String getType() {
        return type;
    }

    public String getReason() {
        return reason;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setQuantityChanged(int quantityChanged) {
        this.quantityChanged = quantityChanged;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
