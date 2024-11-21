package com.liquidtory.app.dto;

public class InventorySubmissionResponse {

    // Vars
    private String firstName;
    private String lastName;
    private String timestamp;

    // Constructors
    public InventorySubmissionResponse() {
    }

    public InventorySubmissionResponse(String firstName, String lastName, String timestamp) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.timestamp = timestamp;
    }

    // Gets and Sets
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

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
