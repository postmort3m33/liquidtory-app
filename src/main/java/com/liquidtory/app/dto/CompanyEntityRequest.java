package com.liquidtory.app.dto;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

public class CompanyEntityRequest {

    // Vars
    private String name;
    private String address;
    private String email;
    private String ownerPhone;
    private String businessPhone;

    // Constructors
    public CompanyEntityRequest() {
    }

    public CompanyEntityRequest(String name, String address, String email, String ownerPhone, String businessPhone) {
        this.name = name;
        this.address = address;
        this.email = email;
        this.ownerPhone = ownerPhone;
        this.businessPhone = businessPhone;
    }

    // Gets and Sets
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOwnerPhone() {
        return ownerPhone;
    }

    public void setOwnerPhone(String ownerPhone) {
        this.ownerPhone = ownerPhone;
    }

    public String getBusinessPhone() {
        return businessPhone;
    }

    public void setBusinessPhone(String businessPhone) {
        this.businessPhone = businessPhone;
    }
}
