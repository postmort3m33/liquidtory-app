package com.liquidtory.app.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Bar")
public class BarEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private CompanyEntity company;

    @Column(nullable = true)
    private Long lastSubmissionId;

    @OneToMany(mappedBy = "bar", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LiquorBottleItem> liquorBottleItems = new ArrayList<>();

    // Constructors
    public BarEntity() {
    }

    public BarEntity(String name, CompanyEntity company) {
        this.name = name;
        this.setCompany(company);
    }

    // gets and Sets
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

    public CompanyEntity getCompany() { return company; }

    public void setCompany(CompanyEntity company) {

        // Set this company
        this.company = company;

        // add this bar to that company
        if (company != null && !company.getBars().contains(this)) {
            company.addBar(this);
        }
    }

    public Long getLastSubmissionId() { return lastSubmissionId; }

    public void setLastSubmissionId(Long lastSubmissionId) { this.lastSubmissionId = lastSubmissionId; }

    public List<LiquorBottleItem> getLiquorBottleItems() { return liquorBottleItems; }

    public void setLiquorBottleItems(List<LiquorBottleItem> liquorBottleItems) { this.liquorBottleItems = liquorBottleItems; }

    //////////////////////
    // Helper methods.. //
    //////////////////////

    // Add Liquor Bottle Item
    public Boolean addLiquorBottleItem(LiquorBottleItem item) {

        // Add this LiquorBottle Item to the Array
        liquorBottleItems.add(item);

        // set This bar to this bar for this Liquor Bottle item
        item.setBar(this);

        // Return
        return true;
    }

    // Remove Single Bottle
    public Boolean removeLiquorBottleItem(LiquorBottleItem liquorBottleItem) {

        // Loop Through
        for (LiquorBottleItem inventoryItem: liquorBottleItems) {

            // If Id is the same
            if (inventoryItem.getLiquorBottle().getId().equals(liquorBottleItem.getLiquorBottle().getId()) && inventoryItem.getCurrentML().equals(liquorBottleItem.getCurrentML())) {

                // Remove this instance
                liquorBottleItems.remove(inventoryItem);

                // Leave loop
                return true;
            }
        }

        // False if not found..
        return false;
    }

    // Remove All Items (Clear Inventory)
    public void removeLiquorBottleItems() {

        // Remove this Liquor Bottle Item from the Array
        liquorBottleItems.clear();
    }
}

