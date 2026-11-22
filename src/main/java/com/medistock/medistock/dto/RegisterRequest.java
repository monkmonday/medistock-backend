package com.medistock.medistock.dto;

import lombok.Data;

@Data
public class RegisterRequest {
    private String name;
    private String email;
    private String password;
    private String role; // "ADMIN", "PHARMACIST", "CASHIER"
    private Long branchId; // nullable for ADMIN
}