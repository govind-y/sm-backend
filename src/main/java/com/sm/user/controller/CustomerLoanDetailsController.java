package com.sm.user.controller;

import com.sm.user.document.CustomerLoan;
import com.sm.user.document.dto.LoanDetailRequest;
import com.sm.user.repository.CustomerLoanRepository;
import com.sm.user.service.CommonService;
import com.sm.user.utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api")
public class CustomerLoanDetailsController {

    @Autowired
    private CustomerLoanRepository customerLoanRepository;

    @PostMapping("/customerloan")
    public ResponseEntity<?> save(@RequestBody LoanDetailRequest loanDetailRequest){
       return ResponseEntity.ok(customerLoanRepository.save(convertToEntity(loanDetailRequest)));
    }

    private CustomerLoan convertToEntity(LoanDetailRequest loanDetailRequest) {
        CustomerLoan loan= new CustomerLoan();
        loan.setAmount(loanDetailRequest.getAmount());
        loan.setCustomerId(loanDetailRequest.getCustomerId());
        loan.setLoanType(loanDetailRequest.getLoanType());
        loan.setSession(CommonUtils.getCurrentSessionYear());
        loan.setStoreId(loanDetailRequest.getStoreId());
        loan.setTransactionDate(LocalDateTime.now());
        if(loanDetailRequest.getId()==null){
            loan.setCreatedDateTimeStamp(LocalDateTime.now());
        }
        loan.setId(loanDetailRequest.getId());
        loan.setTransactionType("DEBIT");
        return loan;
    }
}
