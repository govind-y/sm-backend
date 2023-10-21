package com.sm.user.controller;

import com.sm.user.document.Items;
import com.sm.user.document.LotSoldSchedule;
import com.sm.user.document.ProductIn;
import com.sm.user.document.ProductOut;
import com.sm.user.document.dto.ItemDetails;
import com.sm.user.document.dto.ProductOutRequest;
import com.sm.user.document.dto.Response;
import com.sm.user.repository.ItemsRepository;
import com.sm.user.repository.LotSoldScheduleRepository;
import com.sm.user.repository.ProductInRepository;
import com.sm.user.repository.ProductOutRepository;
import com.sm.user.service.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.PATCH;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    private LotSoldScheduleRepository soldScheduleRepository;


    @Autowired
    private CommonService commonService;

    @PostMapping("/productout")
    @Transactional
    public ResponseEntity<Response> save(@RequestBody ProductOutRequest productOutRequest){
        ProductOut productOut=new ProductOut();
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
//        Optional<LotSoldSchedule> finalSoldSchedule = soldSchedule;
        productOutRequest.getItemIds().forEach(item-> itemsRepository.setProductOutId(productOut1.getId(), productOutRequest.getSoldScheduleId(),item.getWeight(),item.getItemId()));
        List<ProductIn> productIns = productInRepository.findAllByStoreIdAndLotNo(productOut1.getStoreId(),productOut.getLotNo());
        ProductIn productIn = productIns.iterator().next();
        productIns.iterator().next().setCurrentQuantity(String.valueOf((Long.valueOf(productIn.getQuantity())-Long.valueOf(productOutRequest.getQuantity()))));
        productInRepository.save(productIn);


        settlementAmountAfterProductOut(productOutRequest, productOut);


        return ResponseEntity.ok(new Response("Product out successfully with list of items " +productOutRequest.getItemIds().size()+" to businessMan "+productOut.getSoldBussinessManId(),200+""));
    }



    @GetMapping("/items/lotScheduleId/{lotScheduleId}")
    public ResponseEntity<List<?>> findAllByItemDetailsByLotScheduleId(@PathVariable("lotScheduleId") Long lotScheduleId){
        List<Items> items = itemsRepository.findAllByLotScheduleId(lotScheduleId);
        if(!CollectionUtils.isEmpty(items)){
            return ResponseEntity.ok( items.stream().map(item->{
                ItemDetails itemDetails = new ItemDetails();
                itemDetails.setId(item.getId());
                itemDetails.setWeight(item.getWeight());
                itemDetails.setProductOutId(String.valueOf(item.getProductOutId()));
                itemDetails.setItemNo(item.getItemNo());
                itemDetails.setWeight(item.getWeight());
                return itemDetails;
            }).collect(Collectors.toList()));
        }
       return ResponseEntity.ok(new ArrayList<>());
    }

    private void settlementAmountAfterProductOut(ProductOutRequest productOutRequest, ProductOut productOut) {
        if(CollectionUtils.isEmpty(productOutRequest.getItemIds()) || productOutRequest.getSoldBusinessManId()==null){
            throw new BadRequestException("Please add item details !");
        }
        Optional<LotSoldSchedule> soldSchedule;
        List<LotSoldSchedule> soldSchedules = soldScheduleRepository.findByLotNo(productOutRequest.getLotNo());
        if(!CollectionUtils.isEmpty(soldSchedules)){
            List<Items> items = itemsRepository.findAllByLotScheduleId(productOutRequest.getSoldScheduleId());
            Long count = 0l;
            if(!CollectionUtils.isEmpty(items)){
                count= items.stream().filter(item->item.getWeight()!=null).count();

            }
            soldSchedule = soldSchedules.stream().filter(item->item.getSoldStatus().equalsIgnoreCase("IN_PROGRESS") ||item.getSoldStatus().equalsIgnoreCase("WEIGHT_IN_PROGRESS")).findFirst();
          Long weightTillNow= productOutRequest.getItemIds().size()+count;
             if(soldSchedule.isPresent() &&(soldSchedule.get().getSoldQuantity()==productOutRequest.getItemIds().size() ||
                    soldSchedule.get().getSoldQuantity()==weightTillNow)){
                soldSchedule.get().setSoldStatus("WEIGHTED");

            }else{
                soldSchedule.get().setSoldStatus("WEIGHT_IN_PROGRESS");
            }
            if(soldSchedule.isPresent() && productOutRequest.getSoldBusinessManId()==null){
                productOut.setSoldBussinessManId(soldSchedule.get().getSupplierId());
            }else{
                productOut.setSoldBussinessManId(productOutRequest.getSoldBusinessManId());
            }

            calculateSettlememnt(soldSchedule.get());
            soldScheduleRepository.save(soldSchedule.get());
        }else {
            throw new BadRequestException("No in progress request found!");
        }
    }


    private void calculateSettlememnt(LotSoldSchedule lotSoldSchedule){

            List<Items> items = itemsRepository.findAllByLotScheduleId(lotSoldSchedule.getId());
            if(CollectionUtils.isEmpty(items)){
                return;
            }
            List<Items> filteredValue = items.stream().filter(items1 -> items1.getWeight() != null && items1.getProductOutId() != null).collect(Collectors.toList());

            double totalWeight = filteredValue.stream().filter(item->item.getWeight()>0 && item.getWeight()!=null).mapToDouble(item -> item.getWeight()).sum();
            double totalAmount = totalWeight>0?totalWeight/53 * lotSoldSchedule.getPrice():0;

            lotSoldSchedule.setTotalAmount(Math.round(totalAmount));
            lotSoldSchedule.setStoreCharge( lotSoldSchedule.getSoldQuantity()*105);
            lotSoldSchedule.setWeightCharge(filteredValue.size()*1.25);
            lotSoldSchedule.setPaidAmount(Math.round(totalAmount- (lotSoldSchedule.getSoldQuantity()*105+filteredValue.size()*1.25)));
            if(lotSoldSchedule.getSoldStatus().equalsIgnoreCase("WEIGHTED")){
                lotSoldSchedule.setPaymentDate(LocalDate.now().plusDays(15l));
            }

            lotSoldSchedule.setWeightDate(LocalDate.now());
            soldScheduleRepository.save(lotSoldSchedule);

    }





}
