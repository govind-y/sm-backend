package com.sm.user.document.dto;

import lombok.Data;

@Data
public class SoldScheduleRequest {
    private Long id;
    private Long customerId;
    private String storeId;
    private String lotNo;
    private Long soldQuantity;
    private Long soldOutQuantity;
    private Double price;
    private String soldType;//full, partial
    private Long supplierId;
}
