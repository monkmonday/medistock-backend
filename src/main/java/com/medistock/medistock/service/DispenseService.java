package com.medistock.medistock.service;

import com.medistock.medistock.entity.*;
import com.medistock.medistock.repository.BatchRepository;
import com.medistock.medistock.repository.DispenseRecordRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DispenseService {

    private final BatchRepository batchRepository;
    private final DispenseRecordRepository dispenseRecordRepository;

    public DispenseService(BatchRepository batchRepository,
                           DispenseRecordRepository dispenseRecordRepository) {
        this.batchRepository = batchRepository;
        this.dispenseRecordRepository = dispenseRecordRepository;
    }

    @Transactional
    public List<DispenseRecord> dispense(Long drugId, Long branchId, int quantityRequested,
                                         String prescriptionRef, User dispensedBy) {

        List<Batch> availableBatches = batchRepository.findAll().stream()
                .filter(b -> b.getDrug().getId().equals(drugId))
                .filter(b -> b.getBranch().getId().equals(branchId))
                .filter(b -> b.getQuantity() > 0)
                .sorted(Comparator.comparing(Batch::getExpiryDate))
                .collect(Collectors.toList());

        int remaining = quantityRequested;
        List<DispenseRecord> records = new java.util.ArrayList<>();

        for (Batch batch : availableBatches) {
            if (remaining <= 0) break;

            int takeFromThisBatch = Math.min(remaining, batch.getQuantity());

            batch.setQuantity(batch.getQuantity() - takeFromThisBatch);
            batchRepository.save(batch);

            DispenseRecord record = new DispenseRecord();
            record.setDrug(batch.getDrug());
            record.setBatch(batch);
            record.setBranch(batch.getBranch());
            record.setDispensedBy(dispensedBy);
            record.setQuantity(takeFromThisBatch);
            record.setPrescriptionRef(prescriptionRef);
            record.setDispensedAt(LocalDateTime.now());

            dispenseRecordRepository.save(record);
            records.add(record);

            remaining -= takeFromThisBatch;
        }

        if (remaining > 0) {
            throw new RuntimeException("Insufficient stock: short by " + remaining + " units");
        }

        return records;
    }
}