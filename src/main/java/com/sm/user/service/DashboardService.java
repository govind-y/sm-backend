package com.sm.user.service;

import com.sm.user.document.dto.DashboardCountResponse;
import com.sm.user.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.Tuple;
import java.math.BigInteger;

@Component
public class DashboardService {
@Autowired
    private CustomerRepository customerRepository;

public DashboardCountResponse getDashboardCount(String sessionYear){
    Tuple ss = customerRepository.getCountOfDashboardData(sessionYear);
    DashboardCountResponse countResponse = new DashboardCountResponse(
            ss.get(0, BigInteger.class),
            ss.get(1, Double.class),
            ss.get(2, Double.class),
            ss.get(3, Double.class)
            );
return countResponse;

}

    public Double getProductInDetailByBrandType(String brandType){

        return customerRepository.totalAvailableQualityProductIn(brandType);

    }
    public Double getProductOutDetailByBrandType(String brandType){

        return customerRepository.totalAvailableQualityProductOut(brandType);

    }

    public Double getProductInDetailByRoomNo(String roomNo){

        return customerRepository.totalAvailableQualityProductInByRoom(roomNo);

    }

    public Double getProductOutDetailByRoomNo(String roomNO){

        return customerRepository.totalAvailableQualityProductOutByRoom(roomNO);

    }
}
