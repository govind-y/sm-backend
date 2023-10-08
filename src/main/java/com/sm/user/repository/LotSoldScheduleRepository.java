package com.sm.user.repository;

import com.sm.user.document.LotSoldSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface LotSoldScheduleRepository extends JpaRepository<LotSoldSchedule,Long> {

List<LotSoldSchedule> findByLotNo(String lotNo);
    List<LotSoldSchedule> findAllByStoreIdAndSoldDate(String lotNo, LocalDate soldDate);
    List<LotSoldSchedule> findAllByStoreId(String storeId);
    List<LotSoldSchedule> findAllByStoreIdAndCustomerId(String storeId, Long customerId);

}
