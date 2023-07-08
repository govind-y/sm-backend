package com.sm.user.repository;

import com.sm.user.document.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer,Long> {

   Customer  findByPhone(String phone);
   List<Customer> findAllByStoreId(String storeId);
}
