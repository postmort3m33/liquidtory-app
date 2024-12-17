package com.liquidtory.app.entity;

import jakarta.persistence.*;

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

    // Constructors
    public LiquorBottle() {
    }

    public LiquorBottle(String name, Long capacityML) {
        this.name = name;
        this.capacityML = capacityML;
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
}
