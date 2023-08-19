package com.sm.user.service;

import com.sm.user.document.RoomLotDetails;
import com.sm.user.repository.RoomLotDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommonService {
    @Autowired
    private RoomLotDetailsRepository detailsRepository;


    public RoomLotDetails getAvailableLotDetails(String lotNo){
        RoomLotDetails roomLotDetails = detailsRepository.findAllByGeneratedLotName(lotNo);
        if(roomLotDetails.getCurrentLotCapacity()>0){
            return roomLotDetails;
        }
        return null;
    }


    public List<RoomLotDetails> getAvailableLotDetails(List<RoomLotDetails> lotDetails){

         if(!CollectionUtils.isEmpty(lotDetails)){
            lotDetails.removeIf(item->!StringUtils.isEmpty(item.getAllocatedCustomer()) && item.getCurrentLotCapacity()==item.getLotCapacity());
        }
        return lotDetails;
    }

    public String getCurrentSession(){
        return String.valueOf(LocalDate.now().getYear());
    }
}
