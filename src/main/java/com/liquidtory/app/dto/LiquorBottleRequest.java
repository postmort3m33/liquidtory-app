package com.liquidtory.app.dto;

import java.util.List;

public class LiquorBottleRequest {

    // Vars
    private String name;
    private Long capacityML;
    private Double heightCM;
    private Double diameterBottomCM;
    private List<LiquorBottleDimensionDto> dimensions;

    // Constructor
    public LiquorBottleRequest() {
    }

    public LiquorBottleRequest(String name, Long capacityML, Double heightCM, Double diameterBottomCM, List<LiquorBottleDimensionDto> dimensions) {
        this.name = name;
        this.capacityML = capacityML;
        this.heightCM = heightCM;
        this.diameterBottomCM = diameterBottomCM;
        this.dimensions = dimensions;
    }

    // Gets and Sets
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCapacityML() {
        return capacityML;
    }

    public void setCapacityML(Long capacityML) {
        this.capacityML = capacityML;
    }

    public Double getHeightCM() {
        return heightCM;
    }

    public void setHeightCM(Double heightCM) {
        this.heightCM = heightCM;
    }

    public Double getDiameterBottomCM() {
        return diameterBottomCM;
    }

    public void setDiameterBottomCM(Double diameterBottomCM) {
        this.diameterBottomCM = diameterBottomCM;
    }

    public List<LiquorBottleDimensionDto> getDimensions() {
        return dimensions;
    }

    public void setDimensions(List<LiquorBottleDimensionDto> dimensions) {
        this.dimensions = dimensions;
    }
}
