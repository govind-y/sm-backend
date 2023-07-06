package com.sm.user.document.dto;

import com.sm.user.document.RoomLotDetails;
import lombok.Data;

import java.util.List;

@Data
public class AvailableRooms {
    private String roomNo;
    private List<RoomLotDetails> roomLotDetails;
}
