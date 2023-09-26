package com.sm.user.service;

import com.sm.user.document.RoomLotDetails;
import com.sm.user.repository.ProductInRepository;
import com.sm.user.repository.RoomLotDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class CommonService {
    @Autowired
    private RoomLotDetailsRepository detailsRepository;
    @Autowired
    private ProductInRepository productInRepository;


    public RoomLotDetails getAvailableLotDetails(String lotNo){
        RoomLotDetails roomLotDetails = detailsRepository.findAllByGeneratedLotName(lotNo);
        if(roomLotDetails.getCurrentLotCapacity()>0){
            return roomLotDetails;
        }
        return null;
    }


    public List<RoomLotDetails> getAvailableLotDetails(List<RoomLotDetails> lotDetails){

         if(!CollectionUtils.isEmpty(lotDetails)){
//          String storeId=    lotDetails.iterator().next().getStoreId();
//             lotDetails.stream().map(RoomLotDetails::getRoomNo).forEach(roomNo->{
//                 storeId
//             });
         //    productInRepository.findAllByStoreIdAndRoomNo()
            lotDetails.removeIf(item->!StringUtils.isEmpty(item.getAllocatedCustomer()) && item.getCurrentLotCapacity()==item.getLotCapacity());
        }
        return lotDetails;
    }

    public String getCurrentSession(){
        return String.valueOf(LocalDate.now().getYear());
    }


    public String getAuthKey(String key){
        Collection<? extends GrantedAuthority> authorities =  Optional.ofNullable(SecurityContextHolder.getContext()).map(SecurityContext::getAuthentication)
                .map(Authentication::getAuthorities).orElse(new ArrayList<>());

        Optional<? extends GrantedAuthority> authority = authorities.stream().filter(dd -> dd.getAuthority().contains(key)).findFirst();
        if(authority.isPresent()){
           return authority.get().getAuthority().split(":")[1].trim();
        }
        return null;
    }

}
