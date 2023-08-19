package com.sm.user.repository;

import com.sm.user.document.CustomerLoan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerLoanRepository extends JpaRepository<CustomerLoan , Long> {

    List<CustomerLoan> findAllByStoreId(String storeId);
}
