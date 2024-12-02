package com.liquidtory.app.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "AdminInventoryAction")
public class AdminInventoryAction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Column(nullable = false)
    private String actionType; // e.g., "ADD_BOTTLE" or "REMOVE_BOTTLE"

    @Column(nullable = false)
    private Long liquorBottleId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity performedBy; // Admin user who performed the action

    @ManyToOne
    @JoinColumn(name = "bar_id", nullable = false)
    private BarEntity bar;

    @Column(nullable = true)
    private String notes; // Optional notes about the action

    @Column(nullable = false)
    private Boolean successful;

    // Constructor, Getters, and Setters
    public AdminInventoryAction() {
    }

    public AdminInventoryAction(LocalDateTime timestamp, String actionType, Long liquorBottleId, UserEntity performedBy, BarEntity bar, String notes, Boolean successful) {
        this.timestamp = timestamp;
        this.actionType = actionType;
        this.liquorBottleId = liquorBottleId;
        this.performedBy = performedBy;
        this.bar = bar;
        this.notes = notes;
        this.successful = successful;
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

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public Long getLiquorBottleId() {
        return liquorBottleId;
    }

    public void setLiquorBottleId(Long liquorBottleId) {
        this.liquorBottleId = liquorBottleId;
    }

    public UserEntity getPerformedBy() {
        return performedBy;
    }

    public void setPerformedBy(UserEntity performedBy) {
        this.performedBy = performedBy;
    }

    public BarEntity getBar() {
        return bar;
    }

    public void setBar(BarEntity bar) {
        this.bar = bar;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Boolean getSuccessful() { return successful; }

    public void setSuccessful(Boolean successful) { this.successful = successful; }
}
