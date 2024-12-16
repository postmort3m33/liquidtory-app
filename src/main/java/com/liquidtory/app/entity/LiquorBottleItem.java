package com.liquidtory.app.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "LiquorBottleItem")
public class LiquorBottleItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Reference to the type of liquor bottle
    @ManyToOne(optional = false)
    @JoinColumn(name = "type_id")
    private LiquorBottle liquorBottle;

    // Current amount of liquid in the bottle
    @Column(nullable = false)
    private Long currentML;

    // Bar this item is associated with..
    @ManyToOne
    @JoinColumn(name = "bar_id")
    private BarEntity bar;

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

    public BarEntity getBar() { return bar; }

    public void setBar(BarEntity bar) { this.bar = bar; }
}
