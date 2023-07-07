package com.sm.user.transformer;

import com.sm.user.document.RoomLotDetails;
import com.sm.user.document.dto.AvailableRooms;
import com.sm.user.document.dto.ColumnDetails;
import com.sm.user.document.dto.FloorDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RoomTransformer {

    public List<AvailableRooms> convertLotDetailsResponse(List<RoomLotDetails>  lotDetails){
        Map<String, List<RoomLotDetails>> roomsMap = lotDetails.stream().collect(Collectors.groupingBy(RoomLotDetails::getRoomNo));
       return roomsMap.entrySet().stream().map(roomLotDetail->{
            AvailableRooms availableRooms= new AvailableRooms();
            availableRooms.setRoomNo(roomLotDetail.getKey());
            availableRooms.setFloorDetails(roomLotDetail.getValue().stream().collect(Collectors.groupingBy(RoomLotDetails::getFloorNo)).entrySet().stream().map(item->{
                FloorDetails floorDetails= new FloorDetails();
                floorDetails.setFloorNo(item.getKey());
                floorDetails.setColumnDetails(item.getValue().stream().map(columnDetails->{
                    ColumnDetails columnDetail=new ColumnDetails();
                    columnDetail.setColumnNo(columnDetails.getColumnNo());
                    columnDetail.setCurrentLotCapacity(columnDetails.getCurrentLotCapacity());
                    columnDetail.setInitLotCapacity(columnDetails.getLotCapacity());
                    return columnDetail;
                }).collect(Collectors.toList()));
                return floorDetails;
            }).collect(Collectors.toList()));
           return availableRooms;

        }).collect(Collectors.toList());
    }
}
