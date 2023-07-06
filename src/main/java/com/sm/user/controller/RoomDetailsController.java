package com.sm.user.controller;

import com.sm.user.document.RoomLotDetails;
import com.sm.user.document.Store;
import com.sm.user.document.dto.AvailableRooms;
import com.sm.user.repository.RoomLotDetailsRepository;
import com.sm.user.repository.StoreRepository;
import com.sm.user.service.RoomDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RestController
@RequestMapping("/api/lots")

@CrossOrigin(origins = "*",exposedHeaders = "*",allowedHeaders = "*")
public class RoomDetailsController {

    @Autowired
    private RoomDetailsService  roomDetailsService;
    @Autowired
    private RoomLotDetailsRepository roomLotDetailsRepository;


    @PostMapping("/generate/{storeId}")
    public ResponseEntity<String> generateLots(@PathVariable("storeId") String storeId) {
        roomDetailsService.generateRoomLots(storeId);
        return ResponseEntity.ok("Lot generated successfully" );
    }


    @GetMapping("/lotDetails/{roomNo}/store/{storeId}")
    public ResponseEntity<List<RoomLotDetails>>  getRoomLotDetails(@PathVariable String roomNo,@PathVariable String storeId, @RequestParam(required = false) String session){

        if(StringUtils.isEmpty(session)){
            session=String.valueOf( LocalDate.now().getYear());
        }
        List<RoomLotDetails> roomLots = roomLotDetailsRepository.findAllByRoomNoAndStoreIdAndSession(roomNo, storeId, session);
        if(CollectionUtils.isEmpty(roomLots)){
            generateLots(storeId);
            roomLots = roomLotDetailsRepository.findAllByRoomNoAndStoreIdAndSession(roomNo, storeId, session);
        }
        return ResponseEntity.ok(roomLots);

    }


    @GetMapping("/available/store/{storeId}")
    public ResponseEntity<List<AvailableRooms>>  getAvailable(@PathVariable String storeId, @RequestParam(required = false) String session){

        if(StringUtils.isEmpty(session)){
            session=String.valueOf( LocalDate.now().getYear());
        }
        List<RoomLotDetails> roomLotDetails = roomLotDetailsRepository.findAllByStoreIdAndSession(storeId, session);
if(CollectionUtils.isEmpty(roomLotDetails)){
    return ResponseEntity.ok(new ArrayList<>());
}
        Map<String, List<RoomLotDetails>> availableRooms = roomLotDetails.stream().filter(item -> StringUtils.isEmpty(item.getAllocatedCustomer())).collect(Collectors.groupingBy(RoomLotDetails::getRoomNo));
        List<AvailableRooms> availableRooms2 = availableRooms.entrySet().stream().map(item -> {
            AvailableRooms availableRooms1 = new AvailableRooms();
            availableRooms1.setRoomNo(item.getKey());
            availableRooms1.setRoomLotDetails(item.getValue());
            return availableRooms1;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(availableRooms2);

    }



}