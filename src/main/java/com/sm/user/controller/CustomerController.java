package com.sm.user.controller;

import com.sm.user.document.Customer;
import com.sm.user.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api")

@CrossOrigin(origins = "*",exposedHeaders = "*",allowedHeaders = "*")
public class CustomerController {
    @Autowired
    private CustomerRepository customerRepository;
    @PostMapping("/customer")
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer){

        if(customer.getRegisterSession()==null){
            customer.setRegisterSession(String.valueOf(LocalDateTime.now().getYear()));
        }
       Customer customer1= customerRepository.findByPhoneOrCustomerNumber(customer.getPhone(),customer.getCustomerNumber());
       if(ObjectUtils.isEmpty(customer1)){
           customer.setCreatedDateTimeStamp(LocalDateTime.now());
           customer.setUpdatedTimeStamp(LocalDateTime.now());
           return ResponseEntity.ok(customerRepository.save(customer));
       }
       customer.setId(customer1.getId());
       customer.setPhone(customer1.getPhone());
       customer.setUpdatedTimeStamp(LocalDateTime.now());
       customer.setCustomerNumber(customer1.getCustomerNumber());
        return ResponseEntity.ok(customerRepository.save(customer));
    }

    @GetMapping("/customer/storeId/{storeId}")
    public ResponseEntity<List<Customer>> getCustomerByStoreId(@PathVariable("storeId") String storeId){

       return ResponseEntity.ok(customerRepository.findAllByStoreId(storeId));
    }

    @GetMapping("/customer/phone/{phone}")
    public ResponseEntity<Customer> getCustomerByPhoneOrCustomer(@PathVariable("phone") String phone){

        return ResponseEntity.ok(customerRepository.findByPhoneOrCustomerNumber(phone,phone));
    }

}
