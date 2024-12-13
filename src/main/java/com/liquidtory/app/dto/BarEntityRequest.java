package com.liquidtory.app.dto;

public class BarEntityRequest {

    // Vars
    private String name;

    // Constructors
    public BarEntityRequest() {
    }

    public BarEntityRequest(String name) {
        this.name = name;
    }

    // Gets and Sets
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
