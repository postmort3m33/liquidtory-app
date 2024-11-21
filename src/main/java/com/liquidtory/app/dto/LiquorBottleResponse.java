package com.liquidtory.app.dto;

public class LiquorBottleResponse {

    // Vars
    private Long id;
    private String name;
    private Long capacityML;

    // Constructor
    public LiquorBottleResponse() {
    }

    public LiquorBottleResponse(Long id, String name, Long capacityML) {
        this.id = id;
        this.name = name;
        this.capacityML = capacityML;
    }

    // Gets and Sets
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
