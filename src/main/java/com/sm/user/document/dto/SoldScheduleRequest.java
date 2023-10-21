package com.sm.user.document.dto;

import lombok.Data;
import org.jetbrains.annotations.NotNull;

@Data
public class SoldScheduleRequest {
    private Long id;
    @NotNull
    private Long customerId;
    @NotNull
    private String storeId;
    @NotNull
    private String lotNo;
    private Long soldQuantity;
    @NotNull
    private Double price;
    @NotNull
    private String soldType;//full, partial
    @NotNull
    private Long supplierId;
}
