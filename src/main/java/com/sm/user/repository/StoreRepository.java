package com.sm.user.repository;

import com.sm.user.document.Store;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<Store,Long> {

    Store findByStoreIdOrPhone(String storeId, String phone);
}
