
package com.sm.user.document.dto;

import lombok.Data;

import java.util.List;

@Data
public class LotDetails{
    private String lotId;
    private String lotNo;
    private Long productInId;
    private String productType;
    private String productSize;
    private Long availableQuantity;
    private Integer totalQuantity;
    private List<ItemDetails> itemDetails;
    private String lotStatus;

}