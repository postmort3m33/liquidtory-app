package com.liquidtory.app.dto;

import com.liquidtory.app.entity.InventorySnapshot;

import java.util.List;

public class InventorySubmissionResponse {

    // Vars
    private Long id;
    private String firstName;
    private String lastName;
    private String barName;
    private String timestamp;
    private List<InventorySnapshotDto> snapshots;

    // Constructors
    public InventorySubmissionResponse() {
    }

    public InventorySubmissionResponse(Long id, String firstName, String lastName, String barName, String timestamp, List<InventorySnapshotDto> snapshots) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.barName = barName;
        this.timestamp = timestamp;
        this.snapshots = snapshots;
    }

    // Gets and Sets
    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

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

    public String getBarName() { return barName; }

    public void setBarName(String barName) { this.barName = barName; }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public List<InventorySnapshotDto> getSnapshots() { return snapshots; }

    public void setSnapshots(List<InventorySnapshotDto> snapshots) { this.snapshots = snapshots; }
}
