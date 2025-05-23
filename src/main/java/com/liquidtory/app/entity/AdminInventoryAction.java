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

    @Column(nullable = true)
    private Long amountMls;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

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

    public AdminInventoryAction(LocalDateTime timestamp, String actionType, Long liquorBottleId, Long amountMls, String firstName, String lastName, BarEntity bar, String notes, Boolean successful) {
        this.timestamp = timestamp;
        this.actionType = actionType;
        this.liquorBottleId = liquorBottleId;
        this.amountMls = amountMls;
        this.firstName = firstName;
        this.lastName = lastName;
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

    public Long getAmountMls() { return amountMls; }

    public void setAmountMls(Long amountMls) { this.amountMls = amountMls; }

    public String getFirstName() { return firstName; }

    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }

    public void setLastName(String lastName) { this.lastName = lastName;    }

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
