package com.liquidtory.app.repository;

import com.liquidtory.app.entity.CompanyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyEntityRepository extends JpaRepository<CompanyEntity, Long> {
}
