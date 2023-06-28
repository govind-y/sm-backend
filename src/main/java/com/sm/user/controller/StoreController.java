package com.sm.user.controller;


import com.sm.user.document.RegistrationSubscription;
import com.sm.user.document.Store;
import com.sm.user.repository.RegistrationSubscriptionRepository;
import com.sm.user.repository.StoreRepository;
import com.sm.user.service.OtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;


@CrossOrigin(origins = "*",exposedHeaders = "*",allowedHeaders = "*")
@RestController
@RequestMapping("/api")
public class StoreController {
@Autowired
    private StoreRepository storeRepository;
@Autowired
private RegistrationSubscriptionRepository subscriptionRepository;
@Autowired
    OtpService otpService;


    @PostMapping("/open/store")
    public ResponseEntity<Store> create(@RequestBody Store store){
        if(store.getRegistrationSessionYear()==null){
            store.setRegistrationSessionYear(String.valueOf(LocalDate.now().getYear()));
        }
        if(StringUtils.isEmpty(store.getStoreId())){
            store.setStoreId(UUID.randomUUID().toString());
        }
        store.getRoomDetails().stream().forEach(room->{
            if(StringUtils.isEmpty(room.getRoomId())){
                room.setRoomId(UUID.randomUUID().toString());
            }
        });
        Store store1= storeRepository.findByStoreIdOrPhone(store.getStoreId(),store.getPhone());
      if(ObjectUtils.isEmpty(store1)) {
          store.setCreatedDateTimeStamp(LocalDateTime.now());
          store.setUpdatedTimeStamp(LocalDateTime.now());
         return  ResponseEntity.ok(storeRepository.save(store));
      }
        RegistrationSubscription subscription = subscriptionRepository.findByStoreKey(store.getRegistrationKey());
      if( !(subscription!=null && store.getRegistrationKey().equals(subscription.getStoreKey()) && subscription.getStatus().equals("ACTIVE"))){
          return ResponseEntity.badRequest().eTag("Failed due to invalid key").build();
      }
      store.setId(store1.getId());
        store.setStoreId(store1.getStoreId());
        store.setPhone(store1.getPhone());
        store.setUpdatedTimeStamp(LocalDateTime.now());
        return  ResponseEntity.ok( storeRepository.save(store));
    }


    @GetMapping("/allstore")
    public ResponseEntity<List<Store>> stores(){
        return ResponseEntity.ok(new ArrayList<>(storeRepository.findAll()));
    }

    @CrossOrigin(origins = "*",exposedHeaders = "*",allowedHeaders = "*")
    @GetMapping("/dd/hello")
    public ResponseEntity<String> hello(){
        return ResponseEntity.ok("Hello working");
    }

    @GetMapping("/open/generateOtp")
    public ResponseEntity<String> generateOtp(@RequestParam String mob){
        int otp = otpService.generateOTP(mob);
        return ResponseEntity.ok("Generated Otp : "+otp);
    }


}
