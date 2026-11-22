package com.medistock.medistock.dto;

import lombok.Data;

@Data
public class DispenseRequest {
    private Long drugId;
    private Long branchId;
    private int quantity;
    private String prescriptionRef;
}