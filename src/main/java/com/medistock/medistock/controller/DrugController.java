package com.medistock.medistock.controller;

import com.medistock.medistock.entity.Drug;
import com.medistock.medistock.repository.DrugRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/drugs")
public class DrugController {

    private final DrugRepository drugRepository;

    public DrugController(DrugRepository drugRepository) {
        this.drugRepository = drugRepository;
    }

    @GetMapping
    public List<Drug> getAllDrugs() {
        return drugRepository.findAll();
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Drug createDrug(@RequestBody Drug drug) {
        return drugRepository.save(drug);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteDrug(@PathVariable Long id) {
        drugRepository.deleteById(id);
    }
}