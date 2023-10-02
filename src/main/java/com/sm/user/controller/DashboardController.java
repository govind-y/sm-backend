package com.sm.user.controller;

import com.sm.user.document.dto.DashboardCustomerResponse;
import com.sm.user.document.dto.ResponseData;
import com.sm.user.service.CommonService;
import com.sm.user.service.DashboardService;
import com.sm.user.token.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*", exposedHeaders = "*", allowedHeaders = "*")
public class DashboardController {
    @Autowired
    DashboardService  dashboardService;
    @Autowired
    JwtTokenUtil jwtTokenUtil;
    @Autowired
    CommonService commonService;

@GetMapping("/dashboardCount/session/{sessionYear}")
    public ResponseEntity<?> dashboardCount(@PathVariable("sessionYear") String sessionYear){
    String storeId = commonService.getAuthKey("storeId");
    if(storeId==null){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    return ResponseEntity.ok(dashboardService.getDashboardCount(sessionYear,storeId));
}


@GetMapping("/productInCount/identifierType/{identifierType}/identifier/{identifier}")
    public ResponseEntity<ResponseData> getProductInCountBasedOnIdentifier(@PathVariable("identifierType") String identifierType, @PathVariable("identifier") String identifier){
    String storeId = commonService.getAuthKey("storeId");
    if(storeId==null){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
   if(identifierType.equalsIgnoreCase("brand")){
       return ResponseEntity.ok(new ResponseData(dashboardService.getProductInDetailByBrandType(identifier,storeId)));
   }else if(identifierType.equalsIgnoreCase("room")){
       return ResponseEntity.ok(new ResponseData(dashboardService.getProductInDetailByRoomNo(identifier,storeId)));
   }else{
       return ResponseEntity.badRequest().eTag("No identifier matched").build();
   }

}

    @GetMapping("/productOutCount/identifierType/{identifierType}/identifier/{identifier}")
    public ResponseEntity<ResponseData> getProductOutCountBasedOnIdentifier(@PathVariable("identifierType") String identifierType, @PathVariable("identifier") String identifier){
        String storeId = commonService.getAuthKey("storeId");
        if(storeId==null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if(identifierType.equalsIgnoreCase("brand")){
            return ResponseEntity.ok(new ResponseData(dashboardService.getProductOutDetailByBrandType(identifier,storeId)));
        }else if(identifierType.equalsIgnoreCase("room")){
            return ResponseEntity.ok(new ResponseData(dashboardService.getProductOutDetailByRoomNo(identifier,storeId)));
        }else{
            return ResponseEntity.badRequest().eTag("No identifier matched").build();
        }

    }


    @GetMapping("/customerResponse/session/{session}/averageRate/{averageRate}")
    public ResponseEntity<List<DashboardCustomerResponse>> getCustomerResponse(@PathVariable("session") String session, @PathVariable("averageRate") Long averageRate, @RequestParam(required = false) Long stroeCharge){
       return   ResponseEntity.ok(dashboardService.getDashboardResponse(session, averageRate,stroeCharge==null?105:stroeCharge));

    }
}
