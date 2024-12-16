package com.liquidtory.app.dto;

public class AdminActionResponse {

    // Vars
    private Long id;
    private String firstName;
    private String lastName;
    private String barName;
    private String timestamp;
    private String actionType;
    private String bottleDesc;
    private String notes;

    //Constructor
    public AdminActionResponse() {
    }

    public AdminActionResponse(Long id, String firstName, String lastName, String barName, String timestamp, String actionType, String bottleDesc, String notes) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.barName = barName;
        this.timestamp = timestamp;
        this.actionType = actionType;
        this.bottleDesc = bottleDesc;
        this.notes = notes;
    }

    // Gets and Sets

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getFirstName() { return firstName; }

    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }

    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getTimestamp() { return timestamp; }

    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public String getBottleDesc() { return bottleDesc; }

    public void setBottleDesc(String bottleDesc) { this.bottleDesc = bottleDesc; }

    public String getBarName() { return barName; }

    public void setBarName(String barName) { this.barName = barName; }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
