package com.sm.user.controller;

import com.sm.user.document.Customer;
import com.sm.user.document.LotSoldSchedule;
import com.sm.user.document.ProductIn;
import com.sm.user.document.dto.Response;
import com.sm.user.document.dto.SoldScheduleRequest;
import com.sm.user.repository.CustomerRepository;
import com.sm.user.repository.LotSoldScheduleRepository;
import com.sm.user.repository.ProductInRepository;
import com.sm.user.utils.CommonUtils;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.BadRequestException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*",exposedHeaders = "*",allowedHeaders = "*")
public class LotSoldDetailsController {

    @Autowired
    private LotSoldScheduleRepository soldScheduleRepository;
    @Autowired
    private ProductInRepository productInRepository;
    @Autowired
    private CustomerRepository customerRepository;

    @PostMapping("/soldSchedule")
    public ResponseEntity<?> save(@RequestBody SoldScheduleRequest soldScheduleRequest){
        try {
            return ResponseEntity.ok(soldScheduleRepository.save(convertToEntity(soldScheduleRequest)));
        }catch (Exception e){
            throw new BadRequestException(e.getMessage());
        }

    }


    @GetMapping("/soldSchedule/storeId/{storeId}")
    public ResponseEntity<?> findAllByStoreId(@PathVariable("storeId") String storeId,@RequestParam(required = false) Long customerId){
        List<LotSoldSchedule> lotSoldSchedules;

        if(customerId!=null){
              lotSoldSchedules = soldScheduleRepository.findAllByStoreIdAndCustomerId(storeId, customerId);

        }else{
            lotSoldSchedules= soldScheduleRepository.findAllByStoreId(storeId);
        }
        lotSoldSchedules.stream().forEach(lotSchedule->{
            if (lotSchedule.getCustomerId() != null) {
                Optional<Customer> customer = customerRepository.findById(lotSchedule.getCustomerId());
                lotSchedule.setCustomerName(customer.isPresent() ? customer.get().getFirstName() + " " + (customer.get().getLastName() != null ? customer.get().getLastName() : "") : "");
            }
            if(lotSchedule.getSupplierId() != null){
                Optional<Customer> supplier = customerRepository.findById(lotSchedule.getSupplierId());
                lotSchedule.setSupplierName(supplier.isPresent() ? supplier.get().getFirstName() + " " + (supplier.get().getLastName() != null ? supplier.get().getLastName() : "") : "");
            }
        });
        return ResponseEntity.ok(lotSoldSchedules);
    }



    @GetMapping("/averagePrice/storeId/{storeId}")
    public ResponseEntity<?> findAllAveragePriceByStoreId(@PathVariable("storeId") String storeId,@RequestParam(required = false) LocalDate priceDate){
        if(priceDate!=null){
            List<LotSoldSchedule> lotSoldSchedules = soldScheduleRepository.findAllByStoreIdAndSoldDate(storeId, priceDate);
            if(CollectionUtils.isEmpty(lotSoldSchedules)){
                Response response= new Response(String.valueOf(0),"No Market value found");
                return ResponseEntity.ok(response);
            }
            double averagePrice = lotSoldSchedules.stream().mapToDouble(item -> item.getPrice()).average().getAsDouble();
            Response response= new Response(String.valueOf(averagePrice),"Success");
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.ok(soldScheduleRepository.findAllByStoreId(storeId));
    }


    private LotSoldSchedule convertToEntity(SoldScheduleRequest soldScheduleRequest) {


        List<ProductIn> productIns = productInRepository.findAllByStoreIdAndLotNo(soldScheduleRequest.getStoreId(), soldScheduleRequest.getLotNo());
        List<LotSoldSchedule> soldSchedules = soldScheduleRepository.findByLotNo(soldScheduleRequest.getLotNo());

        Double totalInQuanity = productIns.stream().mapToDouble(prodcutin -> Double.parseDouble(prodcutin.getQuantity())).sum();
        Double totalSoldQuanity = soldSchedules.stream().filter(item->!item.getSoldStatus().equalsIgnoreCase("CANCELLED") && item.getSoldQuantity()!=null).mapToDouble(prodcutin -> prodcutin.getSoldQuantity()).sum();
      if(!((totalInQuanity-soldScheduleRequest.getSoldQuantity())<=totalSoldQuanity)){
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
