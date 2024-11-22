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

    @Column(nullable = true)
    private Long lastSubmissionId;

    @OneToMany(mappedBy = "bar", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LiquorBottleItem> liquorBottleItems = new ArrayList<>();

    // Constructors
    public BarEntity() {
    }

    public BarEntity(String name) {
        this.name = name;
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

    public Long getLastSubmissionId() { return lastSubmissionId; }

    public void setLastSubmissionId(Long lastSubmissionId) { this.lastSubmissionId = lastSubmissionId; }

    public List<LiquorBottleItem> getLiquorBottleItems() { return liquorBottleItems; }

    public void setLiquorBottleItems(List<LiquorBottleItem> liquorBottleItems) { this.liquorBottleItems = liquorBottleItems; }

    // Helper methods..
    public void addLiquorBottleItem(LiquorBottleItem item) {

        // Add this LiquorBottle Item to the Array
        liquorBottleItems.add(item);

        // set This bar to this bar for this Liquor Bottle item
        item.setBar(this);
    }

    public void removeLiquorBottleItems() {

        // Remove this Liquor Bottle Item from the Array
        liquorBottleItems.clear();
    }
}

