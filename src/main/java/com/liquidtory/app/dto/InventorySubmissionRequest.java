package com.liquidtory.app.dto;

import java.util.List;

public class InventorySubmissionRequest {

    // Vars
    private List<LiquorBottleItemDto> liquorBottleItemsToSubmit;
    private Long barId;

    // Constructor
    public InventorySubmissionRequest() {
    }

    public InventorySubmissionRequest(List<LiquorBottleItemDto> liquorBottleItemsToSubmit, Long barId) {
        this.liquorBottleItemsToSubmit = liquorBottleItemsToSubmit;
        this.barId = barId;
    }

    // Gets and Sets
    public List<LiquorBottleItemDto> getLiquorBottleItemsToSubmit() {
        return liquorBottleItemsToSubmit;
    }

    public void setLiquorBottleItemsToSubmit(List<LiquorBottleItemDto> liquorBottleItemsToSubmit) {
        this.liquorBottleItemsToSubmit = liquorBottleItemsToSubmit;
    }

    public Long getBarId() {
        return barId;
    }

    public void setBarId(Long barId) {
        this.barId = barId;
    }
}
