package com.sm.user.repository;

import com.sm.user.document.RoomLotDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.math.RoundingMode;
import java.util.List;

public interface RoomLotDetailsRepository extends JpaRepository<RoomLotDetails,Long> {

    List<RoomLotDetails> findAllByRoomNoAndStoreIdAndSession( String roomId, String storeId, String sessionYear);
    List<RoomLotDetails> findAllByStoreIdAndSession(  String storeId, String sessionYear);
    RoomLotDetails findAllByGeneratedLotName(String lotName);

    void deleteByStoreId(String storeId);
    @Modifying
    @Query(value = "update room_lot_details set allocated_customer=:customerNo current_lot_capacity=:currentLotCapacity where generated_lot_name= :lotNo", nativeQuery = true)
    int updateCustomerNo(String lotNo, Integer currentLotCapacity, String customerNo);


 }
