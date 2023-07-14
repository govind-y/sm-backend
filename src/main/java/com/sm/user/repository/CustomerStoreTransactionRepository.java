package com.sm.user.repository;

import com.sm.user.document.CustomerStoreTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerStoreTransactionRepository extends JpaRepository<CustomerStoreTransaction,Long> {

    List<CustomerStoreTransaction> findAllByCustomerId(Long customerId);
}
