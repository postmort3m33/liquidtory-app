package com.liquidtory.app.repository;

import com.liquidtory.app.entity.AdminInventoryAction;
import com.liquidtory.app.entity.InventorySubmission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface InventorySubmissionRepository extends JpaRepository<InventorySubmission, Long> {
    List<InventorySubmission> findAllByTimestampBetween(LocalDateTime startDate, LocalDateTime endDate);
}
