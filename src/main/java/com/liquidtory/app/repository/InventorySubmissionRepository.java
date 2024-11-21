package com.liquidtory.app.repository;

import com.liquidtory.app.entity.InventorySubmission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventorySubmissionRepository extends JpaRepository<InventorySubmission, Long> {

    // Find latest Submission based on Timestamp
    InventorySubmission findTopByOrderByTimestampDesc();
}
