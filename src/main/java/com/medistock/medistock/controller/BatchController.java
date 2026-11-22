package com.medistock.medistock.controller;

import com.medistock.medistock.entity.Batch;
import com.medistock.medistock.entity.Role;
import com.medistock.medistock.entity.User;
import com.medistock.medistock.repository.BatchRepository;
import com.medistock.medistock.security.CustomUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/batches")
public class BatchController {

    private final BatchRepository batchRepository;

    public BatchController(BatchRepository batchRepository) {
        this.batchRepository = batchRepository;
    }

    @GetMapping
    public List<Batch> getBatches(@AuthenticationPrincipal CustomUserDetails principal) {
        User user = principal.getUser();

        if (user.getRole() == Role.ADMIN) {
            return batchRepository.findAll();
        }

        return batchRepository.findAll().stream()
                .filter(batch -> batch.getBranch().getId().equals(user.getBranch().getId()))
                .collect(Collectors.toList());
    }

    @PostMapping
    public Batch createBatch(@RequestBody Batch batch, @AuthenticationPrincipal CustomUserDetails principal) {
        User user = principal.getUser();

        if (user.getRole() != Role.ADMIN) {
            batch.setBranch(user.getBranch());
        }

        return batchRepository.save(batch);
    }
}