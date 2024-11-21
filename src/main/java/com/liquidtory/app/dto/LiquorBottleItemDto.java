package com.liquidtory.app.dto;

public class LiquorBottleItemDto {

    // Vars
    private Long liquorBottleId;
    private Long currentML;

    // Constructor
    public LiquorBottleItemDto() {
    }

    public LiquorBottleItemDto(Long liquorBottleId, Long currentML) {
        this.liquorBottleId = liquorBottleId;
        this.currentML = currentML;
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
}
