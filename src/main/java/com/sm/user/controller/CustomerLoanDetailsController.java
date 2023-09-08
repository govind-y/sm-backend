package com.sm.user.controller;

import com.sm.user.document.Customer;
import com.sm.user.document.CustomerLoan;
import com.sm.user.document.LotSoldSchedule;
import com.sm.user.document.dto.LoanDetailRequest;
import com.sm.user.document.dto.LoanDetailsResponse;
import com.sm.user.repository.CustomerLoanRepository;
import com.sm.user.repository.CustomerRepository;
import com.sm.user.service.CommonService;
import com.sm.user.utils.CommonUtils;
import org.checkerframework.common.reflection.qual.GetClass;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")

@CrossOrigin(origins = "*", exposedHeaders = "*", allowedHeaders = "*")
public class CustomerLoanDetailsController {

    @Autowired
    private CustomerLoanRepository customerLoanRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    CommonService commonService;


    @PostMapping("/customerloan")
    public ResponseEntity<?> save(@RequestBody LoanDetailRequest loanDetailRequest) {
        return ResponseEntity.ok(customerLoanRepository.save(convertToEntity(loanDetailRequest)));
    }


    @GetMapping("/customerloan/storeId/{storeId}")
    public ResponseEntity<?> FindAll(@PathVariable String storeId) {
        List<CustomerLoan> customerLoans = customerLoanRepository.findAllByStoreId(storeId);
        if (!CollectionUtils.isEmpty(customerLoans)) {
            return ResponseEntity.ok(customerLoans.stream().map(this::convertToResponse).collect(Collectors.toList()));
        }
        return ResponseEntity.ok(new ArrayList<>());
    }

    @GetMapping("/customerloan/customer/{customerId}")
    public ResponseEntity<?> FindAllByCustomerId(@PathVariable("customerId") Long customerId) {
        List<CustomerLoan> customerLoans = customerLoanRepository.findAllByCustomerId(customerId);
        if (!CollectionUtils.isEmpty(customerLoans)) {
            return ResponseEntity.ok(customerLoans.stream().map(this::convertToResponse).collect(Collectors.toList()));
        }
        return ResponseEntity.ok(new ArrayList<>());
    }

    private LoanDetailsResponse convertToResponse(CustomerLoan customerLoan) {

        LoanDetailsResponse response = new DozerBeanMapper().map(customerLoan, LoanDetailsResponse.class);
        if (customerLoan.getCustomerId() != null) {
            Optional<Customer> customer = customerRepository.findById(customerLoan.getCustomerId());
            response.setCustomerName(customer.isPresent() ? customer.get().getFirstName() + " " + (customer.get().getLastName() != null ? customer.get().getLastName() : "") : "");
        }
        return response;
    }

    private CustomerLoan convertToEntity(LoanDetailRequest loanDetailRequest) {

        CustomerLoan entity = new DozerBeanMapper().map(loanDetailRequest, CustomerLoan.class);
        entity.setTransactionDate(LocalDateTime.now());
        if (loanDetailRequest.getId() == null) {
            entity.setCreatedDateTimeStamp(LocalDateTime.now());
        }
        entity.setSession(commonService.getCurrentSession());
        entity.setId(loanDetailRequest.getId());
        entity.setTransactionType("DEBIT");
        return entity;
    }
}
