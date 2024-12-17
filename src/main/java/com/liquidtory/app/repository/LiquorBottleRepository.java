package com.liquidtory.app.repository;

import com.liquidtory.app.entity.LiquorBottle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LiquorBottleRepository extends JpaRepository<LiquorBottle, Long> {
    boolean existsByNameAndCapacityML(String name, Long capacityML);
}
