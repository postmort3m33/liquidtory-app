package com.liquidtory.app.repository;

import com.liquidtory.app.entity.BarEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BarRepository extends JpaRepository<BarEntity, Long> {

    // Define a query method to find a Bar by its name
    BarEntity findByName(String name);
}
