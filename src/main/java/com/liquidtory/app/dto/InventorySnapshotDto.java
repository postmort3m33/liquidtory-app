package com.liquidtory.app.dto;

public class InventorySnapshotDto {

    private String liquorBottleName; // LiquorBottle's ID
    private Long currentML; // LiquorBottleItem's current amount

    // Constructors

    public InventorySnapshotDto() {
    }

    public InventorySnapshotDto(String liquorBottleName, Long currentML) {
        this.liquorBottleName = liquorBottleName;
        this.currentML = currentML;
    }

    // Gets and Sets

    public String getLiquorBottleName() {
        return liquorBottleName;
    }

    public void setLiquorBottleName(String liquorBottleName) {
        this.liquorBottleName = liquorBottleName;
    }

    public Long getCurrentML() {
        return currentML;
    }

    public void setCurrentML(Long currentML) {
        this.currentML = currentML;
    }
}
