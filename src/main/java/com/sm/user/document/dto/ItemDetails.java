package com.sm.user.document.dto;

import lombok.Data;

@Data
public class ItemDetails {
    private Long id;
    private Integer itemNo;
    private Integer weight;
    private String productInId;
    private Integer productOutId;


}
