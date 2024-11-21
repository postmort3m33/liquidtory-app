package com.liquidtory.app.entity;

import jakarta.persistence.Embeddable;

@Embeddable
public class InventorySnapshot {

    private Long liquorBottleId; // LiquorBottle's ID
    private Long currentML; // LiquorBottleItem's current amount

    // Constructors

    public InventorySnapshot() {
    }

    public InventorySnapshot(Long liquorBottleId, Long currentML) {
        this.liquorBottleId = liquorBottleId;
        this.currentML = currentML;
    }

    // Getters and Setters
    public Long getLiquorBottleId() {
        return liquorBottleId;
    }

    public void setLiquorBottleId(Long liquorBottleId) {
        this.liquorBottleId = liquorBottleId;
    }

    public Long getCurrentML() {
        return currentML;
    }

    public void setCurrentML(Long currentML) {
        this.currentML = currentML;
    }
}
