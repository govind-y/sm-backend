package com.sm.user.repository;

import com.sm.user.document.RoomLotDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.RoundingMode;
import java.util.List;

public interface RoomLotDetailsRepository extends JpaRepository<RoomLotDetails,Long> {

    List<RoomLotDetails> findAllByRoomNoAndStoreIdAndSession( String roomId, String storeId, String sessionYear);
    List<RoomLotDetails> findAllByStoreIdAndSession(  String storeId, String sessionYear);
    RoomLotDetails findAllByGeneratedLotName(String lotName);

    void deleteByStoreId(String storeId);
 }
