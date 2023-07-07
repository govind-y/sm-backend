package com.sm.user.document.dto;

import lombok.Data;

import java.util.List;

@Data
public class FloorDetails {
    private Integer floorNo;
    private List<ColumnDetails> columnDetails;

}
