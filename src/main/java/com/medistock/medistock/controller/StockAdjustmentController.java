package com.medistock.medistock.controller;

import com.medistock.medistock.dto.StockAdjustmentRequest;
import com.medistock.medistock.entity.*;
import com.medistock.medistock.repository.BatchRepository;
import com.medistock.medistock.repository.StockAdjustmentRepository;
import com.medistock.medistock.security.CustomUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/adjustments")
public class StockAdjustmentController {

    private final BatchRepository batchRepository;
    private final StockAdjustmentRepository stockAdjustmentRepository;

    public StockAdjustmentController(BatchRepository batchRepository,
                                     StockAdjustmentRepository stockAdjustmentRepository) {
        this.batchRepository = batchRepository;
        this.stockAdjustmentRepository = stockAdjustmentRepository;
    }

    @PostMapping
    public StockAdjustment adjustStock(@RequestBody StockAdjustmentRequest request,
                                       @AuthenticationPrincipal CustomUserDetails principal) {

        User user = principal.getUser();
        Batch batch = batchRepository.findById(request.getBatchId())
                .orElseThrow(() -> new RuntimeException("Batch not found"));

        if (user.getRole() != Role.ADMIN &&
                !batch.getBranch().getId().equals(user.getBranch().getId())) {
            throw new RuntimeException("Cannot adjust stock for another branch");
        }

        int oldQuantity = batch.getQuantity();
        batch.setQuantity(request.getNewQuantity());
        batchRepository.save(batch);

        StockAdjustment adjustment = new StockAdjustment();
        adjustment.setBatch(batch);
        adjustment.setAdjustedBy(user);
        adjustment.setOldQuantity(oldQuantity);
        adjustment.setNewQuantity(request.getNewQuantity());
        adjustment.setReason(request.getReason());
        adjustment.setAdjustedAt(LocalDateTime.now());

        return stockAdjustmentRepository.save(adjustment);
    }

    @GetMapping
    public List<StockAdjustment> getAdjustments() {
        return stockAdjustmentRepository.findAll();
    }
}