package com.liquidtory.app.dto;

import java.util.List;

public class BarEntityResponse {

    // Vars
    private Long id;
    private String name;
    private LastInventorySubmissionResponse lastSubmission;
    private List<LiquorBottleItemDto> liquorBottleItems;

    // Constructor
    public BarEntityResponse() {
    }

    public BarEntityResponse(Long id, String name, LastInventorySubmissionResponse lastSubmission, List<LiquorBottleItemDto> liquorBottleItems) {
        this.id = id;
        this.name = name;
        this.lastSubmission = lastSubmission;
        this.liquorBottleItems = liquorBottleItems;
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

    public LastInventorySubmissionResponse getLastSubmission() {
        return lastSubmission;
    }

    public void setLastSubmission(LastInventorySubmissionResponse lastSubmission) {
        this.lastSubmission = lastSubmission;
    }

    public List<LiquorBottleItemDto> getLiquorBottleItems() {
        return liquorBottleItems;
    }

    public void setLiquorBottleItems(List<LiquorBottleItemDto> liquorBottleItems) {
        this.liquorBottleItems = liquorBottleItems;
    }
}
