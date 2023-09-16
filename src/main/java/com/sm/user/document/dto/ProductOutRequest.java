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
    @NotNull
    private String reasonOfOut;
    @NotNull
    private Long  customerId;
    @NotNull
    private Long  soldBusinessManId;
    @NotNull
    private String storeId;
    @NotNull
    private List<ItemDetailsRequest> itemIds;

}
