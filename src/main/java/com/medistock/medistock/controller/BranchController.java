package com.medistock.medistock.controller;

import com.medistock.medistock.entity.Branch;
import com.medistock.medistock.repository.BranchRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/branches")
public class BranchController {

    private final BranchRepository branchRepository;

    public BranchController(BranchRepository branchRepository) {
        this.branchRepository = branchRepository;
    }

    @GetMapping
    public List<Branch> getAllBranches() {
        return branchRepository.findAll();
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Branch createBranch(@RequestBody Branch branch) {
        return branchRepository.save(branch);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteBranch(@PathVariable Long id) {
        branchRepository.deleteById(id);
    }
}