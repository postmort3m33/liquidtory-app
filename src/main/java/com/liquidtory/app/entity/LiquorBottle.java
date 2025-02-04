package com.liquidtory.app.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "LiquorBottle",
       uniqueConstraints = {
               @UniqueConstraint(name = "unique_name_capacity", columnNames = {"name", "capacityML"})
       }
)
public class LiquorBottle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Long capacityML;

    @Column(nullable = false)
    private Double heightCM;

    @Column(nullable = false)
    private Double diameterBottomCM;

    @OneToMany(mappedBy = "liquorBottle", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<LiquorBottleDimension> dimensions = new ArrayList<>();

    // Constructors
    public LiquorBottle() {
    }

    public LiquorBottle(String name, Long capacityML) {
        this.name = name;
        this.capacityML = capacityML;
        this.heightCM = 30.0;
        this.diameterBottomCM = 7.9;

        // Create Default Dimensions
        LiquorBottleDimension dimension1 = new LiquorBottleDimension(0.0, 100.0, this);
        LiquorBottleDimension dimension2 = new LiquorBottleDimension(63.98, 100.0, this);
        LiquorBottleDimension dimension3 = new LiquorBottleDimension(74.56, 38.76, this);
        LiquorBottleDimension dimension4 = new LiquorBottleDimension(100.0, 38.76, this);

        // new List
        List<LiquorBottleDimension> newList = new ArrayList<>();

        // Add to it
        newList.add(dimension1);
        newList.add(dimension2);
        newList.add(dimension3);
        newList.add(dimension4);

        // Set it..
        this.dimensions = newList;

    }

    public LiquorBottle(String name, Long capacityML, Double heightCM, Double diameterBottomCM) {
        this.name = name;
        this.capacityML = capacityML;
        this.heightCM = heightCM;
        this.diameterBottomCM = diameterBottomCM;
    }

    public LiquorBottle(String name, Long capacityML, Double heightCM, Double diameterBottomCM, List<LiquorBottleDimension> dimensions) {
        this.name = name;
        this.capacityML = capacityML;
        this.heightCM = heightCM;
        this.diameterBottomCM = diameterBottomCM;
        this.dimensions = dimensions;
    }

    // Gets and Sets

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

    public Long getCapacityML() {
        return capacityML;
    }

    public void setCapacityML(Long capacityML) {
        this.capacityML = capacityML;
    }

    public Double getHeightCM() {
        return heightCM;
    }

    public void setHeightCM(Double heightCM) {
        this.heightCM = heightCM;
    }

    public Double getDiameterBottomCM() {
        return diameterBottomCM;
    }

    public void setDiameterBottomCM(Double diameterBottomCM) {
        this.diameterBottomCM = diameterBottomCM;
    }

    public List<LiquorBottleDimension> getDimensions() { return dimensions; }

    public void setDimensions(List<LiquorBottleDimension> dimensions) { this.dimensions = dimensions; }
}
