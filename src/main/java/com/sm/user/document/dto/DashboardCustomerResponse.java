package com.sm.user.document.dto;

import lombok.Data;

@Data
public class DashboardCustomerResponse {


    private String customerName;
    private Long productCount;
    private Long averageValueOfProduct;
    private Double totalLoanAmount;
    private String customerType;

}
