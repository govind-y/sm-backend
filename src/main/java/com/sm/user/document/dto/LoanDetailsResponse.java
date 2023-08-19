package com.sm.user.document.dto;

import lombok.Data;

@Data
public class LoanDetailsResponse {

    private Long id;
    private String loanType;
    private long customerId;
    private String customerName;
    private Long rateOfInterest;
    private String storeId;
    private Double amount;

}
