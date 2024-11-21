package com.liquidtory.app.dto;

import java.util.List;

public class InventorySubmissionRequest {

    // Vars
    private List<LiquorBottleItemDto> liquorBottleItemsToSubmit;
    private boolean isOutside;

    // Constructor
    public InventorySubmissionRequest() {
    }

    public InventorySubmissionRequest(List<LiquorBottleItemDto> liquorBottleItemsToSubmit, boolean isOutside) {
        this.liquorBottleItemsToSubmit = liquorBottleItemsToSubmit;
        this.isOutside = isOutside;
    }

    // Gets and Sets
    public List<LiquorBottleItemDto> getLiquorBottleItemsToSubmit() {
        return liquorBottleItemsToSubmit;
    }

    public void setLiquorBottleItemsToSubmit(List<LiquorBottleItemDto> liquorBottleItemsToSubmit) {
        this.liquorBottleItemsToSubmit = liquorBottleItemsToSubmit;
    }

    public boolean getIsOutside() {
        return isOutside;
    }

    public void setIsOutside(boolean outside) {
        isOutside = outside;
    }
}
