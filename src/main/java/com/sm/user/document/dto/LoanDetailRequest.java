package com.sm.user.document.dto;

import lombok.Data;

@Data
public class LoanDetailRequest {
    private Long id;
    private String loanType;
    private long customerId;
    private String storeId;
    private Double amount;
}
