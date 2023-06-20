package com.sm.user.repository;

import com.sm.user.document.CustomerStoreTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerStoreTransactionRepository extends JpaRepository<CustomerStoreTransaction,Long> {
}
