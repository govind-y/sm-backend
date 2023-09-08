package com.sm.user.document.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LoanDetailRequest {
    private Long id;
    private String loanType;
    private long customerId;
    private String storeId;
    private Double amount;
    private Double rateOfInterest;
    private String transactionType;
    private Integer countOfPackets;

}
