package com.sm.user.controller;

import com.sm.user.document.*;
import com.sm.user.document.dto.ItemDetails;
import com.sm.user.document.dto.LotDetails;
import com.sm.user.document.dto.ProductDetails;
import com.sm.user.repository.*;
import com.sm.user.service.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.BadRequestException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RestController
@RequestMapping("/api")

@CrossOrigin(origins = "*", exposedHeaders = "*", allowedHeaders = "*")
public class ProductInController {
    @Autowired
    private ProductInRepository productInRepository;
    @Autowired
    private ItemsRepository itemsRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CommonService commonService;
    @Autowired
    private LotSoldScheduleRepository soldScheduleRepository;
    @Autowired
    RoomLotDetailsRepository roomLotDetailsRepository;

    @PostMapping("/productin")
    @Async
    @Transactional
    public ResponseEntity<?> create(@RequestBody ProductIn productIn) {
        try {
            if (productIn.getProductInDate() == null) {
                productIn.setProductInDate(LocalDate.now());
            }
            RoomLotDetails roomLotDetails = commonService.getAvailableLotDetails(productIn.getLotNo());
            Integer currentLotCapacity = roomLotDetails.getCurrentLotCapacity() - Integer.parseInt(productIn.getQuantity());
            if (ObjectUtils.isEmpty(roomLotDetails) && currentLotCapacity>0) {
                throw new BadRequestException("Lot capacity already occupied");
            }
            productIn.setCreatedDateTimeStamp(LocalDateTime.now());
            productIn.setUpdatedTimeStamp(LocalDateTime.now());

            Integer i = Integer.parseInt(productIn.getQuantity());
            List<Items> items = IntStream.range(1, i+1).mapToObj(intValue -> {
                Items item = new Items();
                item.setItemNo(intValue);
                Product product = new Product();
                product.setId(productIn.getProductId());
                item.setProduct(product);
                item.setLotNo(productIn.getLotNo());
                item.setProductIn(productIn);
                return item;
            }).collect(Collectors.toList());
            productIn.setItems(items);
            productInRepository.save(productIn);
//        itemsRepository.saveAll(items);

            roomLotDetailsRepository.updateCustomerNo(String.valueOf(productIn.getCustomerId()),currentLotCapacity,productIn.getLotNo());
            return ResponseEntity.ok("Product in successfully !");
        }catch (Exception e){
            throw e;
        }

    }

    @GetMapping("/productIn/lookup")
    public ResponseEntity<List<ProductDetails>> getProductDetailsByLotNo(@RequestParam(required = false) String lotNo
            , @RequestParam(required = true) String storeId, @RequestParam(required = false) String roomId) {
        List<ProductIn> productIns = new ArrayList<>();

        if (!StringUtils.isEmpty(lotNo)) {
            productIns = productInRepository.findAllByStoreIdAndLotNo(storeId, lotNo);
        } else if (!StringUtils.isEmpty(roomId)) {
            productIns = productInRepository.findAllByStoreIdAndRoomNo(storeId, roomId);
        } else if (!StringUtils.isEmpty(storeId)) {
            productIns = productInRepository.findAllByStoreId(storeId);
        }

        return ResponseEntity.ok(convertProductDetails(productIns));
    }


    private List<ProductDetails> convertProductDetails(List<ProductIn> productIns) {
        List<Product> products = productRepository.findAll();


        Map<Long, List<ProductIn>> productCustomerMap = productIns.stream().filter(item -> item.getCustomerId() != null).collect(Collectors.groupingBy(ProductIn::getCustomerId));

        return productCustomerMap.entrySet().stream().map(productCus -> {
            ProductDetails productDetails = new ProductDetails();
            productDetails.setCustomerId(productCus.getKey());
            Optional<Customer> customer = customerRepository.findById(productCus.getKey());
            productDetails.setCustomerName(customer.isPresent() ? customer.get().getFirstName() + " " + (customer.get().getLastName()!=null?customer.get().getLastName():"") : "");

            Map<String, List<ProductIn>> map = productCus.getValue().stream().filter(productLot -> productLot.getLotNo() != null).collect(Collectors.groupingBy(ProductIn::getLotNo));
            ;

            productDetails.setLotDetails(map.entrySet().stream().map(item -> {
                LotDetails lotDetails = new LotDetails();
                lotDetails.setLotNo(item.getKey());
                List<LotSoldSchedule> soldSchedule = soldScheduleRepository.findByLotNo(item.getKey());
                if(!CollectionUtils.isEmpty(soldSchedule)){
                    lotDetails.setLotStatus(soldSchedule.iterator().next().getSoldStatus());
                    lotDetails.setSoldBossinessManId(soldSchedule.iterator().next().getSupplierId());
                    Optional<Customer> supplier = customerRepository.findById(soldSchedule.iterator().next().getSupplierId());
                    productDetails.setCustomerName(supplier.isPresent() ? supplier.get().getFirstName() + " " + (supplier.get().getLastName()!=null?supplier.get().getLastName():"") : "");

                }

                ProductIn productIn = item.getValue().iterator().next();
                products.stream().filter(product -> product.getId().equals(productIn.getProductId())).findFirst().ifPresent(product -> {
                    lotDetails.setProductType(product.getProductType());
                    lotDetails.setProductSize(product.getProductSize());
                });
                Set<Items> lotItems = item.getValue().stream()
                        .map(x -> x.getItems())
                        .flatMap(x -> x.stream())
                        .collect(Collectors.toSet());
                lotDetails.setTotalQuantity(lotItems.size());
                lotDetails.setAvailableQuantity(lotItems.size()-lotItems.stream().filter(itemDetail->itemDetail.getProductOutId()!=null && itemDetail.getWeight()!=null && itemDetail.getWeight()>0).count());
                List<ItemDetails> itemDetailsList = lotItems.stream().map(items -> {
                    ItemDetails itemDetails = new ItemDetails();
                    itemDetails.setId(items.getId());
                    itemDetails.setWeight(items.getWeight());
                    itemDetails.setProductOutId(String.valueOf(items.getProductOutId()));
                    itemDetails.setItemNo(items.getItemNo());
                    itemDetails.setWeight(items.getWeight());
                    return itemDetails;
                }).collect(Collectors.toList());
                itemDetailsList.sort(Comparator.comparing(ItemDetails::getItemNo));
                lotDetails.setItemDetails(itemDetailsList);
                lotDetails.setProductInId(productIn.getId());

                return lotDetails;
            }).collect(Collectors.toList()));

            return productDetails;
        }).collect(Collectors.toList());

    }
}
