package com.liquidtory.app.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "LiquorBottleDimension")
public class LiquorBottleDimension {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Double height; // in %

    @Column(nullable = false)
    private Double radius; // in %

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "liquor_bottle_id", nullable = false)
    private LiquorBottle liquorBottle;

    // Constructors
    public LiquorBottleDimension() {
    }

    public LiquorBottleDimension(Double height, Double radius, LiquorBottle liquorBottle) {
        this.height = height;
        this.radius = radius;
        this.liquorBottle = liquorBottle;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public Double getRadius() {
        return radius;
    }

    public void setRadius(Double radius) {
        this.radius = radius;
    }

    public LiquorBottle getLiquorBottle() {
        return liquorBottle;
    }

    public void setLiquorBottle(LiquorBottle liquorBottle) {
        this.liquorBottle = liquorBottle;
    }
}
