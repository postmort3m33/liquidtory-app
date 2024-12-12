package com.liquidtory.app.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Company")
public class CompanyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String ownerPhone;

    @Column(nullable = false)
    private String businessPhone;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BarEntity> bars = new ArrayList<>();

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserEntity> users = new ArrayList<>();

    // Constructors
    public CompanyEntity() {
    }

    public CompanyEntity(String name, String address, String email, String ownerPhone, String businessPhone) {
        this.name = name;
        this.address = address;
        this.email = email;
        this.ownerPhone = ownerPhone;
        this.businessPhone = businessPhone;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOwnerPhone() {
        return ownerPhone;
    }

    public void setOwnerPhone(String phone) {
        this.ownerPhone = phone;
    }

    public String getBusinessPhone() { return businessPhone; }

    public void setBusinessPhone(String businessPhone) { this.businessPhone = businessPhone; }

    public List<BarEntity> getBars() {
        return bars;
    }

    public void setBars(List<BarEntity> bars) {
        this.bars = bars;
    }

    public List<UserEntity> getUsers() {
        return users;
    }

    public void setUsers(List<UserEntity> users) {
        this.users = users;
    }

    // Helper methods..
    public void addBar(BarEntity bar) {

        // Add it to Array
        bars.add(bar);
    }

    // Add user
    public void addUser(UserEntity user) {

        // Add it
        users.add(user);
    }
}
