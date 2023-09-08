package com.sm.user.document.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LoanDetailsResponse {

    private Long id;
    private Long customerId;
    private String storeId;
    private String session;
    private String loanType;
    private Double amount;
    private Double rateOfInterest;
    private String transactionType;
    private Integer countOfPackets;
    private String transactionDate;
    private String customerName;

}
