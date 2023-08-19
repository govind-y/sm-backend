package com.sm.user.controller;

import com.sm.user.document.Customer;
import com.sm.user.document.CustomerStoreTransaction;
import com.sm.user.document.dto.CustomerTransactionResponse;
import com.sm.user.repository.CustomerRepository;
import com.sm.user.repository.CustomerStoreTransactionRepository;
import com.sm.user.transformer.CustomerTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")

@CrossOrigin(origins = "*",exposedHeaders = "*",allowedHeaders = "*")
public class CustomerTransactionDataController {
    @Autowired
    private CustomerStoreTransactionRepository transactionRepository;
    @Autowired
    private CustomerRepository  customerRepository;

    @Autowired
     private CustomerTransformer customerTransformer;

    @PostMapping("/customer/transaction")
    public ResponseEntity<CustomerStoreTransaction> save(@RequestBody CustomerStoreTransaction customerStoreTransaction){
       return ResponseEntity.ok( transactionRepository.save(customerStoreTransaction));
    }


    @GetMapping("/customerTransaction/customerId/{customerId}")
    public ResponseEntity<List<CustomerTransactionResponse>> getCustomerTransactions(@PathVariable Long customerId){
        List<CustomerStoreTransaction> transactions = transactionRepository.findAllByCustomerId(customerId);
     if(!CollectionUtils.isEmpty(transactions)){
         Optional<Customer> customer = customerRepository.findById(customerId);
        return ResponseEntity.ok(customerTransformer.customerTransactionTransformer(customer,transactions));
     }
return ResponseEntity.ok(new ArrayList<>());
    }
}
