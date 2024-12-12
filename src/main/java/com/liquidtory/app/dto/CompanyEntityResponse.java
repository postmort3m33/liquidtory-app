package com.liquidtory.app.dto;

public class CompanyEntityResponse {

    // Vars
    private Long id;
    private String name;

    // constructor
    public CompanyEntityResponse() {
    }

    public CompanyEntityResponse(Long id, String name) {
        this.id = id;
        this.name = name;
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
}
