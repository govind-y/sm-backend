package com.sm.user.controller;

import com.sm.user.document.dto.ResponseData;
import com.sm.user.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*", exposedHeaders = "*", allowedHeaders = "*")
public class DashboardController {
    @Autowired
    DashboardService  dashboardService;

@GetMapping("/dashboardCount/session/{sessionYear}")
    public ResponseEntity<?> dashboardCount(@PathVariable("sessionYear") String sessionYear){
    return ResponseEntity.ok(dashboardService.getDashboardCount(sessionYear));
}


@GetMapping("/productInCount/identifierType/{identifierType}/identifier/{identifier}")
    public ResponseEntity<ResponseData> getProductInCountBasedOnIdentifier(@PathVariable("identifierType") String identifierType, @PathVariable("identifier") String identifier){

   if(identifierType.equalsIgnoreCase("brand")){
       return ResponseEntity.ok(new ResponseData(dashboardService.getProductInDetailByBrandType(identifier)));
   }else if(identifierType.equalsIgnoreCase("room")){
       return ResponseEntity.ok(new ResponseData(dashboardService.getProductInDetailByRoomNo(identifier)));
   }else{
       return ResponseEntity.badRequest().eTag("No identifier matched").build();
   }

}

    @GetMapping("/productOutCount/identifierType/{identifierType}/identifier/{identifier}")
    public ResponseEntity<ResponseData> getProductOutCountBasedOnIdentifier(@PathVariable("identifierType") String identifierType, @PathVariable("identifier") String identifier){

        if(identifierType.equalsIgnoreCase("brand")){
            return ResponseEntity.ok(new ResponseData(dashboardService.getProductOutDetailByBrandType(identifier)));
        }else if(identifierType.equalsIgnoreCase("room")){
            return ResponseEntity.ok(new ResponseData(dashboardService.getProductOutDetailByBrandType(identifier)));
        }else{
            return ResponseEntity.badRequest().eTag("No identifier matched").build();
        }

    }
}
