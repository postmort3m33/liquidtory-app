package com.liquidtory.app.dto;

public class AdminActionRequest {

    // Vars
    private String actionType; //ADD_BOTTLE, REMOVE_BOTTLE, etc.
    private Long liquorBottleId;
    private Long barId;
    private String notes;

    //Constructor
    public AdminActionRequest() {
    }

    public AdminActionRequest(String actionType, Long liquorBottleId, Long barId, String notes) {
        this.actionType = actionType;
        this.barId = barId;
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

    public Long getBarId() { return barId; }

    public void setBarId(Long barId) { this.barId = barId; }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
