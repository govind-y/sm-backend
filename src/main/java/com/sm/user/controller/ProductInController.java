package com.sm.user.controller;

import com.sm.user.document.*;
import com.sm.user.document.dto.ItemDetails;
import com.sm.user.document.dto.LotDetails;
import com.sm.user.document.dto.ProductDetails;
import com.sm.user.repository.CustomerRepository;
import com.sm.user.repository.ProductInRepository;
import com.sm.user.repository.ProductRepository;
import com.sm.user.service.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
import java.util.stream.Stream;

@RestController
@RequestMapping("/api")

@CrossOrigin(origins = "*", exposedHeaders = "*", allowedHeaders = "*")
public class ProductInController {
    @Autowired
    private ProductInRepository productInRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CommonService commonService;

    @PostMapping("/productin")
    public ResponseEntity<?> create(@RequestBody ProductIn productIn) {
        if (productIn.getProductInDate() == null) {
            productIn.setProductInDate(LocalDate.now());
        }
        RoomLotDetails roomLotDetails = commonService.getAvailableLotDetails(productIn.getLotNo());
        if (ObjectUtils.isEmpty(roomLotDetails)) {
            throw new BadRequestException("Not is already occupied");
        }
        Integer i = Integer.parseInt(productIn.getQuantity());
        List<Items> items = IntStream.range(1, i).mapToObj(intValue -> {
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
        productIn.setCreatedDateTimeStamp(LocalDateTime.now());
        productIn.setUpdatedTimeStamp(LocalDateTime.now());
        productInRepository.save(productIn);
        return ResponseEntity.ok("Product in successfully !");
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
            productDetails.setCustomerName(customer.isPresent() ? customer.get().getFirstName() + " " + customer.get().getLastName() : "");

            Map<String, List<ProductIn>> map = productCus.getValue().stream().filter(productLot -> productLot.getLotNo() != null).collect(Collectors.groupingBy(ProductIn::getLotNo));
            ;

            productDetails.setLotDetails(map.entrySet().stream().map(item -> {
                LotDetails lotDetails = new LotDetails();
                lotDetails.setLotNo(item.getKey());
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
                lotDetails.setAvailableQuantity(lotItems.stream().filter(itemDetail->itemDetail.getProductOutId()!=null && itemDetail.getWeight()!=null && itemDetail.getWeight()>0).count());
                lotDetails.setItemDetails(lotItems.stream().map(items -> {
                    ItemDetails itemDetails = new ItemDetails();
                    itemDetails.setId(items.getId());
                    itemDetails.setWeight(items.getWeight());
                    itemDetails.setProductOutId(String.valueOf(items.getProductOutId()));
                    itemDetails.setItemNo(items.getItemNo());
                    itemDetails.setWeight(items.getWeight());
                    return itemDetails;
                }).collect(Collectors.toList()));
                lotDetails.setProductInId(productIn.getId());
                return lotDetails;
            }).collect(Collectors.toList()));

            return productDetails;
        }).collect(Collectors.toList());

    }
}
