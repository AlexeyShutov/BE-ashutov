package com.scloud.inventory.model;

public class ProductAvailability {

    private String uniqId;
    private boolean available;

    private ProductAvailability() {}

    public ProductAvailability(String uniqId) {
        this.uniqId = uniqId;
    }

    public ProductAvailability(String uniqId, boolean available) {
        this.uniqId = uniqId;
        this.available = available;
    }

    public String getUniqId() {
        return uniqId;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}
