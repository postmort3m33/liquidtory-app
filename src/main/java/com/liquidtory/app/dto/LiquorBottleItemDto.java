package com.liquidtory.app.dto;

public class LiquorBottleItemDto {

    // Vars
    private Long liquorBottleId;
    private Long currentML;
    private Long barId;

    // Constructor
    public LiquorBottleItemDto() {
    }

    public LiquorBottleItemDto(Long liquorBottleId, Long currentML, Long barId) {
        this.liquorBottleId = liquorBottleId;
        this.currentML = currentML;
        this.barId = barId;
    }

    // Gets and Sets

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

    public Long getBarId() { return barId; }

    public void setBarId(Long barId) { this.barId = barId; }
}
