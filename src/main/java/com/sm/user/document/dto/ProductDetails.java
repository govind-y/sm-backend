package com.sm.user.document.dto;

import lombok.Data;

import java.util.List;

@Data
public class ProductDetails {
    private String customerName;
    private Long customerId;
    private String lotNo;
    private Long productInId;
    private String productType;
    private String productSize;
    private List<ItemDetails> itemDetails;
}
