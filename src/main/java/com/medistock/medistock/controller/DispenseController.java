package com.medistock.medistock.controller;

import com.medistock.medistock.dto.DispenseRequest;
import com.medistock.medistock.entity.DispenseRecord;
import com.medistock.medistock.entity.Role;
import com.medistock.medistock.entity.User;
import com.medistock.medistock.security.CustomUserDetails;
import com.medistock.medistock.service.DispenseService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dispense")
public class DispenseController {

    private final DispenseService dispenseService;

    public DispenseController(DispenseService dispenseService) {
        this.dispenseService = dispenseService;
    }

    @PostMapping
    public List<DispenseRecord> dispense(@RequestBody DispenseRequest request,
                                         @AuthenticationPrincipal CustomUserDetails principal) {

        User user = principal.getUser();

        Long branchId = request.getBranchId();
        if (user.getRole() != Role.ADMIN) {
            branchId = user.getBranch().getId();
        }

        return dispenseService.dispense(
                request.getDrugId(),
                branchId,
                request.getQuantity(),
                request.getPrescriptionRef(),
                user
        );
    }
}