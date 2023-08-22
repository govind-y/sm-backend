package com.sm.user.document.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.math.BigInteger;

@Data
@AllArgsConstructor
public class DashboardCountResponse {
    private BigInteger customerCount;
    private Double loanAmount;
    private Double totalProductOut;
    private Double totalProductIn;


}
