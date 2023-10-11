package com.sm.user.service;

import com.sm.user.document.RoomLotDetails;
import com.sm.user.document.Store;
import com.sm.user.document.dto.AvailableRooms;
import com.sm.user.repository.RoomLotDetailsRepository;
import com.sm.user.repository.StoreRepository;
import com.sm.user.transformer.RoomTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class RoomDetailsService {

    @Autowired
    private RoomLotDetailsRepository roomLotDetailsRepository;
    @Autowired
    private StoreRepository storeRepository;
@Autowired
private RoomTransformer roomTransformer;
@Autowired
private CommonService commonService;


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
                    String lotName = "R" + roomDetails.getRoomNo() + "F" + row + "C" + col + "S" + store.getRegistrationSessionYear() + "S" + store.getId();
                    RoomLotDetails roomLotDetails = new RoomLotDetails();
                    roomLotDetails.setFloorNo(row);
                    roomLotDetails.setColumnNo(col);
                    roomLotDetails.setRoomNo(roomDetails.getRoomNo());
                    roomLotDetails.setGeneratedLotName(lotName);
                    roomLotDetails.setLotCapacity(roomDetails.getPerLotCapacity());
                    roomLotDetails.setCurrentLotCapacity(roomDetails.getPerLotCapacity());
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

    public  List<AvailableRooms> getAllRooms(String storeId, String sessionYear){
        List<RoomLotDetails> roomLotDetails = roomLotDetailsRepository.findAllByStoreIdAndSession(storeId, sessionYear);
        if(CollectionUtils.isEmpty(roomLotDetails)){
            return new ArrayList<>();
        }
        return  roomTransformer.convertLotDetailsResponse(roomLotDetails);

    }

    public  List<AvailableRooms> getAvailableRooms(String storeId, String sessionYear){
        List<RoomLotDetails> roomLotDetails = roomLotDetailsRepository.findAllByStoreIdAndSession(storeId, sessionYear);
        if(CollectionUtils.isEmpty(roomLotDetails)){
            return new ArrayList<>();
        }
     return  roomTransformer.convertLotDetailsResponse(commonService.getAvailableLotDetails(roomLotDetails));

    }
}
