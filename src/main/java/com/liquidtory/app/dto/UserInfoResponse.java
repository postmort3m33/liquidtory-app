package com.liquidtory.app.dto;

public class UserInfoResponse {

    // Name
    private String firstName;
    private String lastName;
    private String company;

    // Constrcutror
    public UserInfoResponse() {
    }

    public UserInfoResponse(String firstName, String lastName, String company) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.company = company;
    }

    // /Getters and Setters
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCompany() { return company; }

    public void setCompany(String company) { this.company = company; }
}
