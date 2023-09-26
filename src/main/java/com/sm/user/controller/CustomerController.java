package com.sm.user.controller;

import com.sm.user.document.Customer;
import com.sm.user.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.persistence.Tuple;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api")

@CrossOrigin(origins = "*", exposedHeaders = "*", allowedHeaders = "*")
public class CustomerController {
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostMapping("/customer")
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) {

        if (customer.getRegisterSession() == null) {
            customer.setRegisterSession(String.valueOf(LocalDateTime.now().getYear()));
        }
        Customer customer1 = customerRepository.findByPhone(customer.getPhone());
        if (ObjectUtils.isEmpty(customer1)) {
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
    public ResponseEntity<List<Customer>> getCustomerByStoreId(@PathVariable("storeId") String storeId, @RequestParam(required = false) String roleType) {
        if (!StringUtils.isEmpty(roleType)) {
            return ResponseEntity.ok(customerRepository.findAllByStoreIdAndRoleType(storeId, roleType));
        }
        return ResponseEntity.ok(customerRepository.findAllByStoreId(storeId));
    }

    @GetMapping("/customer/phone/{phone}")
    public ResponseEntity<Customer> getCustomerByPhoneOrCustomer(@PathVariable("phone") String phone) {

        return ResponseEntity.ok(customerRepository.findByPhone(phone));
    }

    @GetMapping("/customer/dynamicSearch/{searchParam}")
    public ResponseEntity<List<Customer>> getCustomerBySearch(@PathVariable("searchParam") String searchParam) {
String query= "select * from customer c where  c.store_id='c270d0b3-0069-40a5-9a9a-8b847195d166' and ( c.first_name like '%govind%' or c.last_name sounds like '%govind%'  or c.phone like '%7842%')";

        List<Customer> customers = jdbcTemplate.queryForList(query, Customer.class);

        return ResponseEntity.ok(customers);
    }

}
