package com.medistock.medistock.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class StockAdjustment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "batch_id", nullable = false)
    private Batch batch;

    @ManyToOne
    @JoinColumn(name = "adjusted_by", nullable = false)
    private User adjustedBy;

    private Integer oldQuantity;
    private Integer newQuantity;
    private String reason;
    private LocalDateTime adjustedAt;
}
