package com.sm.user.controller;

import com.sm.user.document.CustomerLoan;
import com.sm.user.document.LotSoldSchedule;
import com.sm.user.document.dto.CustomerTransactionResponse;
import com.sm.user.document.dto.LoanDetailRequest;
import com.sm.user.document.dto.SoldScheduleRequest;
import com.sm.user.repository.CustomerLoanRepository;
import com.sm.user.repository.LotSoldScheduleRepository;
import com.sm.user.utils.CommonUtils;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*",exposedHeaders = "*",allowedHeaders = "*")
public class LotSoldDetailsController {

    @Autowired
    private LotSoldScheduleRepository soldScheduleRepository;

    @PostMapping("/soldSchedule")
    public ResponseEntity<?> save(@RequestBody SoldScheduleRequest soldScheduleRequest){
        return ResponseEntity.ok(soldScheduleRepository.save(convertToEntity(soldScheduleRequest)));
    }

    private LotSoldSchedule convertToEntity(SoldScheduleRequest soldScheduleRequest) {

        LotSoldSchedule entity = new DozerBeanMapper().map(soldScheduleRequest, LotSoldSchedule.class);
        entity.setSession(CommonUtils.getCurrentSessionYear());
        entity.setSoldStatus("IN_PROGRESS");
        entity.setSoldDate(LocalDate.now());
        if(soldScheduleRequest.getId()==null){
            entity.setCreatedDateTimeStamp(LocalDateTime.now());
        }
        return entity;
    }
}
