package com.liquidtory.app.dto;

public class LastInventorySubmissionResponse {

    // Vars
    private String firstName;
    private String lastName;
    private Long barId;
    private String timestamp;

    // Constructors
    public LastInventorySubmissionResponse() {
    }

    public LastInventorySubmissionResponse(String firstName, String lastName, Long barId, String timestamp) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.barId = barId;
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

    public Long getBarId() { return barId; }

    public void setBarId(Long barId) { this.barId = barId; }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
