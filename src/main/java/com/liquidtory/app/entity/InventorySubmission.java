package com.liquidtory.app.entity;

import jakarta.persistence.*;
import com.liquidtory.app.entity.BarEntity;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "InventorySubmission")
public class InventorySubmission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime timestamp; // Store when the inventory was submitted

    @Column(nullable = false)
    private Long userId;

    @ManyToOne
    @JoinColumn(name = "bar_id", nullable = false)
    private BarEntity bar;

    @Column(nullable = false)
    private Long numShotsUsed;

    @ElementCollection
    @CollectionTable(name = "inventory_snapshots", joinColumns = @JoinColumn(name = "inventory_submission_id"))
    private List<InventorySnapshot> inventorySnapshots; // Snapshot data

    //Constructors
    public InventorySubmission() {
    }

    public InventorySubmission(LocalDateTime timestamp, Long userId, BarEntity bar, Long numShotsUsed, List<InventorySnapshot> inventorySnapshots) {
        this.timestamp = timestamp;
        this.userId = userId;
        this.bar = bar;
        this.numShotsUsed = numShotsUsed;
        this.inventorySnapshots = inventorySnapshots;
    }

    // Gets and Sets
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public BarEntity getBar() { return bar; }

    public void setBar(BarEntity bar) { this.bar = bar; }

    public Long getNumShotsUsed() { return numShotsUsed; }

    public void setNumShotsUsed(Long numShotsUsed) { this.numShotsUsed = numShotsUsed; }

    public List<InventorySnapshot> getInventorySnapshots() {
        return inventorySnapshots;
    }

    public void setInventorySnapshots(List<InventorySnapshot> submittedInventory) {
        this.inventorySnapshots = submittedInventory;
    }

    // Helper Methods
    public void addInventorySnapshot(InventorySnapshot snapshot) {
        this.inventorySnapshots.add(snapshot);
    }

    public void removeInventorySnapshot(InventorySnapshot snapshot) {
        this.inventorySnapshots.remove(snapshot);
    }
}
