package com.sm.user.document.dto;

import lombok.Data;

import java.util.List;

@Data
public class ProductDetails {
    private String customerName;
    private Long customerId;
    private String supplierName;
   private List<LotDetails> lotDetails;

}

