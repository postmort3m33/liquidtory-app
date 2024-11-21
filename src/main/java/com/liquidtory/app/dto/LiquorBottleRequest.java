package com.liquidtory.app.dto;

public class LiquorBottleRequest {

    // Vars
    private String name;
    private Long capacityML;

    // Constructor
    public LiquorBottleRequest() {
    }

    public LiquorBottleRequest(String name, Long capacityML) {
        this.name = name;
        this.capacityML = capacityML;
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
}
