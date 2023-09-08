package com.sm.user.repository;

import com.sm.user.document.Items;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemsRepository extends JpaRepository<Items,Long> {

    @Modifying
    @Query("update Items item set item.productOutId = :productOutId ,item.lotScheduleId=:lotScheduledId ,item.weight= :weight where item.id = :id ")
    int setProductOutId(@Param("productOutId") Long productOutId,@Param("lotScheduledId") Long lotScheduledId,@Param("weight") Integer weight, @Param("id") Long id);


    List<Items> findAllByLotScheduleId(Long lotScheduleId);


    //    @Query(value = "select i from Items i where i.productIn.id=:productInId and i.productOutId is not null ")
//    List<Items> findAllByproductInIdAndOutIdNotNull(List<Long> productInId);
}
