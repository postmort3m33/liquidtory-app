package com.liquidtory.app.dto;

public class AdminActionRequest {

    // Vars
    private String actionType; //ADD_BOTTLE, REMOVE_BOTTLE, etc.
    private Long liquorBottleId;
    private String barName;
    private String notes;

    //Constructor
    public AdminActionRequest() {
    }

    public AdminActionRequest(String actionType, Long liquorBottleId, String barName, String notes) {
        this.actionType = actionType;
        this.barName = barName;
        this.notes = notes;
    }

    // Gets and Sets
    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public Long getLiquorBottleId() { return liquorBottleId; }

    public void setLiquorBottleId(Long liquorBottleId) { this.liquorBottleId = liquorBottleId; }

    public String getBarName() { return barName; }

    public void setBarName(String barName) { this.barName = barName; }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
