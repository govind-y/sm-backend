package com.sm.user.repository;

import com.sm.user.document.ProductIn;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductInRepository extends JpaRepository<ProductIn,Long> {

    List<ProductIn> findAllByLotNo(String lotNo);
    List<ProductIn> findAllByStoreIdAndRoomNo(String storeId, String roomNo);
    List<ProductIn> findAllByRoomNo(String roomNo);
    List<ProductIn> findAllByStoreId(String storeId);
}
