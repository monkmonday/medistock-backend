package com.medistock.medistock.dto;

import lombok.Data;

@Data
public class StockAdjustmentRequest {
    private Long batchId;
    private int newQuantity;
    private String reason;
}