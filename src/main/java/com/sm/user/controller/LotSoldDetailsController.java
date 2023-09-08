package com.sm.user.controller;

import com.sm.user.document.LotSoldSchedule;
import com.sm.user.document.ProductIn;
import com.sm.user.document.dto.SoldScheduleRequest;
import com.sm.user.repository.LotSoldScheduleRepository;
import com.sm.user.repository.ProductInRepository;
import com.sm.user.utils.CommonUtils;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.BadRequestException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*",exposedHeaders = "*",allowedHeaders = "*")
public class LotSoldDetailsController {

    @Autowired
    private LotSoldScheduleRepository soldScheduleRepository;
    @Autowired
    private ProductInRepository productInRepository;

    @PostMapping("/soldSchedule")
    public ResponseEntity<?> save(@RequestBody SoldScheduleRequest soldScheduleRequest){
        return ResponseEntity.ok(soldScheduleRepository.save(convertToEntity(soldScheduleRequest)));
    }


    @GetMapping("/soldSchedule/storeId/{storeId}")
    public ResponseEntity<?> findAllByStoreId(@PathVariable("storeId") String storeId){

        return ResponseEntity.ok(soldScheduleRepository.findAllByStoreId(storeId));
    }

    private LotSoldSchedule convertToEntity(SoldScheduleRequest soldScheduleRequest) {


        List<ProductIn> productIns = productInRepository.findAllByStoreIdAndLotNo(soldScheduleRequest.getStoreId(), soldScheduleRequest.getLotNo());
        List<LotSoldSchedule> soldSchedules = soldScheduleRepository.findByLotNo(soldScheduleRequest.getLotNo());

        Double totalInQuanity = productIns.stream().mapToDouble(prodcutin -> Double.parseDouble(prodcutin.getQuantity())).sum();
        Double totalSoldQuanity = soldSchedules.stream().filter(item->!item.getSoldStatus().equalsIgnoreCase("CANCELLED") && item.getSoldQuantity()!=null).mapToDouble(prodcutin -> prodcutin.getSoldQuantity()).sum();
      if((totalInQuanity-soldScheduleRequest.getSoldQuantity())<totalSoldQuanity){
          throw new BadRequestException("Item already sold or selling items are more then available quantity! ");
      }
        LotSoldSchedule entity = new DozerBeanMapper().map(soldScheduleRequest, LotSoldSchedule.class);
        entity.setSession(CommonUtils.getCurrentSessionYear());
        entity.setSoldStatus("IN_PROGRESS");
        entity.setSoldDate(LocalDate.now());
        entity.setSupplierId(soldScheduleRequest.getSupplierId());
        if(soldScheduleRequest.getId()==null){
            entity.setCreatedDateTimeStamp(LocalDateTime.now());
        }
        return entity;
    }
}
