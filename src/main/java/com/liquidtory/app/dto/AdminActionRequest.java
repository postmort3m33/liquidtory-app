package com.liquidtory.app.dto;

public class AdminActionRequest {

    // Vars
    private String actionType; //ADD_BOTTLE, REMOVE_BOTTLE, etc.
    private Long liquorBottleId;
    private String fullOrPartial;
    private Long partialAmount;
    private Long numFullBottles;
    private Long barId;
    private String notes;

    //Constructor
    public AdminActionRequest() {
    }

    public AdminActionRequest(String actionType, Long liquorBottleId, String fullOrPartial, Long partialAmount, Long numFullBottles, Long barId, String notes) {
        this.actionType = actionType;
        this.liquorBottleId = liquorBottleId;
        this.fullOrPartial = fullOrPartial;
        this.partialAmount = partialAmount;
        this.numFullBottles = numFullBottles;
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

    public String getFullOrPartial() { return fullOrPartial; }

    public void setFullOrPartial(String fullOrPartial) { this.fullOrPartial = fullOrPartial; }

    public Long getPartialAmount() { return partialAmount; }

    public void setPartialAmount(Long partialAmount) { this.partialAmount = partialAmount; }

    public Long getNumFullBottles() { return numFullBottles; }

    public void setNumFullBottles(Long numFullBottles) { this.numFullBottles = numFullBottles; }

    public Long getBarId() { return barId; }

    public void setBarId(Long barId) { this.barId = barId; }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
