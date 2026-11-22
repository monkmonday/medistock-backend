package com.medistock.medistock.repository;

import com.medistock.medistock.entity.DispenseRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DispenseRecordRepository extends JpaRepository<DispenseRecord, Long> {
}