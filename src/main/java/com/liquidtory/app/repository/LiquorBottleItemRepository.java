package com.liquidtory.app.repository;

import com.liquidtory.app.entity.LiquorBottleItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LiquorBottleItemRepository extends JpaRepository<LiquorBottleItem, Long> {
}
