package com.liquidtory.app.repository;

import com.liquidtory.app.entity.AdminInventoryAction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AdminInventoryActionRepository extends JpaRepository<AdminInventoryAction, Long> {
    List<AdminInventoryAction> findAllByTimestampBetween(LocalDateTime startDate, LocalDateTime endDate);
}
