package com.sm.user.controller;

import com.sm.user.document.ProductIn;
import com.sm.user.document.ProductOut;
import com.sm.user.document.dto.ProductOutRequest;
import com.sm.user.document.dto.Response;
import com.sm.user.repository.ItemsRepository;
import com.sm.user.repository.ProductInRepository;
import com.sm.user.repository.ProductOutRepository;
import com.sm.user.service.CommonService;
import org.springframework.aop.BeforeAdvice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.ws.rs.BadRequestException;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api")

@CrossOrigin(origins = "*",exposedHeaders = "*",allowedHeaders = "*")
public class ProductOutController {

    @Autowired
    private ProductOutRepository productOutRepository;
    @Autowired
    private ProductInRepository productInRepository;

    @Autowired
    ItemsRepository itemsRepository;
    @Autowired
    private CommonService commonService;

    @PostMapping("/productout")
    @Transactional
    public ResponseEntity<Response> save(@RequestBody ProductOutRequest productOutRequest){
        ProductOut productOut=new ProductOut();
        if(CollectionUtils.isEmpty(productOutRequest.getItemIds())){
            throw new BadRequestException("Please add item details !");
        }
        productOut.setSession(commonService.getCurrentSession());
        productOut.setUpdatedTimeStamp(LocalDateTime.now());
        productOut.setCreatedDateTimeStamp(LocalDateTime.now());
        productOut.setCustomerId(productOutRequest.getCustomerId());
        productOut.setId(productOutRequest.getId());
        productOut.setLotNo(productOutRequest.getLotNo());
        productOut.setQuantity(productOutRequest.getQuantity());
        productOut.setReasonOfOut(productOutRequest.getReasonOfOut());
        productOut.setSoldBussinessManId(productOutRequest.getSoldBusinessManId());
        productOut.setStoreId(productOutRequest.getStoreId());
        ProductOut productOut1 = productOutRepository.save(productOut);
        productOutRequest.getItemIds().forEach(item-> itemsRepository.setProductOutId(productOut1.getId(),item.getWeight(),item.getItemId()));
        List<ProductIn> productIns = productInRepository.findAllByStoreIdAndLotNo(productOut1.getStoreId(),productOut.getLotNo());
        ProductIn productIn = productIns.iterator().next();
        productIns.iterator().next().setCurrentQuantity(String.valueOf((Long.valueOf(productIn.getQuantity())-Long.valueOf(productOutRequest.getQuantity()))));
        productInRepository.save(productIn);

        return ResponseEntity.ok(new Response("Product out successfully with list of items " +productOutRequest.getItemIds().size()+" to businessMan "+productOut.getSoldBussinessManId(),200+""));
    }


}
