package com.liquidtory.app.dto;

public class LiquorBottleDimensionDto {

    // Vars
    private Double height;
    private Double radius;

    // Constructor
    public LiquorBottleDimensionDto() {
    }

    public LiquorBottleDimensionDto(Double height, Double radius) {
        this.height = height;
        this.radius = radius;
    }

    // Gets and Sets
    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public Double getRadius() {
        return radius;
    }

    public void setRadius(Double radius) {
        this.radius = radius;
    }
}
