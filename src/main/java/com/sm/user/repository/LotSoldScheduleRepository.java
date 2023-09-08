package com.sm.user.repository;

import com.sm.user.document.LotSoldSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LotSoldScheduleRepository extends JpaRepository<LotSoldSchedule,Long> {

List<LotSoldSchedule> findByLotNo(String lotNo);
    List<LotSoldSchedule> findByLotNoAndSoldStatus(String lotNo, String soldStatus);
    List<LotSoldSchedule> findAllByStoreId(String storeId);
}
