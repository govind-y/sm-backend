package com.sm.user.document.dto;

import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Data
public class ProductOutRequest {
    private Long id;
    @NotNull
    private String lotNo;
    @NotNull
    private String quantity;

    private String reasonOfOut;
    @NotNull
    private Long  customerId;

    private Long  soldBusinessManId;
    @NotNull
    private String storeId;
    private Long soldScheduleId;
    @NotNull
    private List<ItemDetailsRequest> itemIds;

}
