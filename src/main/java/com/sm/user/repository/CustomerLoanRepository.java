package com.sm.user.repository;

import com.sm.user.document.CustomerLoan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerLoanRepository extends JpaRepository<CustomerLoan , Long> {
}
