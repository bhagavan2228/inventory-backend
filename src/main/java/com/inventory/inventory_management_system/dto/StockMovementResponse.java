package com.inventory.inventory_management_system.dto;

import java.time.LocalDateTime;

public class StockMovementResponse {

    private int quantityChanged;
    private String type;
    private String reason;
    private LocalDateTime timestamp;

    public StockMovementResponse(int quantityChanged,
                                 String type,
                                 String reason,
                                 LocalDateTime timestamp) {
        this.quantityChanged = quantityChanged;
        this.type = type;
        this.reason = reason;
        this.timestamp = timestamp;
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
}
