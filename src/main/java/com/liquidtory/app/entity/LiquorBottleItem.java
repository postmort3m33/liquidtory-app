package com.liquidtory.app.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "InventoryItem")
public class LiquorBottleItem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // Reference to the type of liquor bottle
    @ManyToOne(optional = false)
    @JoinColumn(name = "type_id", referencedColumnName = "id")
    private LiquorBottle liquorBottle;

    // Current amount of liquid in the bottle
    @Column(nullable = false)
    private Long currentML;

    // Constructors
    public LiquorBottleItem() {

    }

    public LiquorBottleItem(LiquorBottle liquorBottle, Long currentML) {
        this.liquorBottle = liquorBottle;
        this.currentML = currentML;
    }

    // Gets and Sets
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LiquorBottle getLiquorBottle() {
        return liquorBottle;
    }

    public void setLiquorBottle(LiquorBottle liquorBottle) {
        this.liquorBottle = liquorBottle;
    }

    public Long getCurrentML() {
        return currentML;
    }

    public void setCurrentML(Long currentML) {
        this.currentML = currentML;
    }
}
