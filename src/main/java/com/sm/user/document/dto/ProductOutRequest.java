package com.sm.user.document.dto;

import lombok.Data;

import java.util.List;

@Data
public class ProductOutRequest {
    private Long id;
    private String lotNo;
    private String quantity;
    private String reasonOfOut;
    private Long  customerId;
    private Long  soldBusinessManId;
    private List<ItemDetailsRequest> itemIds;
}
