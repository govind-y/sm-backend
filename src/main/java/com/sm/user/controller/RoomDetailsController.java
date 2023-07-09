package com.sm.user.controller;

import com.sm.user.document.RoomLotDetails;
import com.sm.user.document.dto.AvailableRooms;
import com.sm.user.repository.RoomLotDetailsRepository;
import com.sm.user.service.RoomDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

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
        roomLots.removeIf(item-> !StringUtils.isEmpty(item.getAllocatedCustomer()));
        return ResponseEntity.ok(roomLots);

    }


    @GetMapping("/available/store/{storeId}")
    public ResponseEntity<List<AvailableRooms>>  getAvailable(@PathVariable String storeId, @RequestParam(required = false) String session){

        if(StringUtils.isEmpty(session)){
            session=String.valueOf( LocalDate.now().getYear());
        }
        return ResponseEntity.ok(roomDetailsService.getAvailableRooms(storeId,session));

    }



}