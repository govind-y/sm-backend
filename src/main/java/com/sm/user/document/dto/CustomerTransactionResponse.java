package com.sm.user.document.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CustomerTransactionResponse {

    private Long id;
    private Long customerId;
    private String customerName;
    private String customerPhone;
    private Double storeCharge;
    private String chargeType;
    private Double amount;
    private Integer quantity;
    private Integer rate;
    private String description;
    private Integer interestRate;
    private String lotNo;
    private String storeId;
     private String chargeStartDate;
     private String chargeEndDate;
}
