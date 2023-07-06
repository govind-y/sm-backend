package com.sm.user.service;

import com.sm.user.document.RoomLotDetails;
import com.sm.user.document.Store;
import com.sm.user.repository.RoomLotDetailsRepository;
import com.sm.user.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Service
public class RoomDetailsService {

    @Autowired
    private RoomLotDetailsRepository roomLotDetailsRepository;
    @Autowired
    private StoreRepository storeRepository;


    public void generateRoomLots(String storeId){
        Store store = storeRepository.findByStoreIdOrPhone(storeId, storeId);
        if(store==null){
            return ;
        }
        List<RoomLotDetails> roomLotDetailsList = new ArrayList<>();
        // room loop
        store.getRoomDetails().stream().forEach(roomDetails -> {
//row loop
            IntStream.range(0, roomDetails.getFloorInRoom()).forEach(row -> {
//col loop
                IntStream.range(0, roomDetails.getColumnInRoom()).forEach(col -> {
                    String lotNo = "R-" + roomDetails.getRoomNo() + "-F-" + row + "-C-" + col + "-S-" + store.getRegistrationSessionYear() + "-S-" + storeId;
                    RoomLotDetails roomLotDetails = new RoomLotDetails();
                    roomLotDetails.setFloorNo(row);
                    roomLotDetails.setRoomNo(roomDetails.getRoomNo());
                    roomLotDetails.setGeneratedLotName(lotNo);
                    roomLotDetails.setLotCapacity(roomDetails.getPerLotCapacity());
                    roomLotDetails.setRoomId(roomDetails.getRoomId());
                    roomLotDetails.setSession(String.valueOf(LocalDate.now().getYear()));
                    roomLotDetails.setStoreId(storeId);
                    roomLotDetailsList.add(roomLotDetails);
                });
            });
        });
        roomLotDetailsRepository.saveAll(roomLotDetailsList);
    }


    public void deleteLots(String storeId){
        roomLotDetailsRepository.deleteByStoreId(storeId);
    }
}
