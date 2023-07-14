package com.sm.user.controller;

import com.sm.user.document.*;
import com.sm.user.document.dto.ItemDetails;
import com.sm.user.document.dto.ProductDetails;
import com.sm.user.repository.CustomerRepository;
import com.sm.user.repository.ProductInRepository;
import com.sm.user.repository.ProductRepository;
import com.sm.user.service.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.BadRequestException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
        if(ObjectUtils.isEmpty(roomLotDetails)){
            throw  new BadRequestException("Not is already occupied");
        }
        Integer i = Integer.parseInt(productIn.getQuantity());
        List<Items> items = IntStream.range(0, i).mapToObj(intValue -> {
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
        return ResponseEntity.ok("");
    }

    @GetMapping("/productIn/lookup")
    public ResponseEntity<List<ProductDetails>> getProductDetailsByLotNo(@RequestParam(required = false) String lotNo
            , @RequestParam(required = true) String storeId, @RequestParam(required = false) String roomId) {
        List<ProductIn> productIns = new ArrayList<>();

        if (!StringUtils.isEmpty(lotNo)) {
            productIns = productInRepository.findAllByStoreIdAndLotNo(storeId,lotNo);
        } else if (!StringUtils.isEmpty(roomId)) {
            productIns = productInRepository.findAllByStoreIdAndRoomNo(storeId,roomId);
        } else if (!StringUtils.isEmpty(storeId)) {
            productIns = productInRepository.findAllByStoreId(storeId);
        }

        return ResponseEntity.ok(convertProductDetails(productIns));
    }


    private List<ProductDetails> convertProductDetails(List<ProductIn> productIns){
       return   productIns.stream().map(productIn -> {
            ProductDetails productDetails= new ProductDetails();
            productDetails.setCustomerId(productIn.getCustomerId());
           Optional<Customer> customer = customerRepository.findById(productIn.getId());
           productDetails.setCustomerName(customer.isPresent()?customer.get().getFirstName()+" "+customer.get().getLastName():"");
           Optional<Product> product = productRepository.findById(productIn.getProductId());
         if(product.isPresent()){
             productDetails.setProductType(product.get().getProductType());
             productDetails.setProductSize(product.get().getProductSize());
         }
           productDetails.setItemDetails( productIn.getItems().stream().map(item->{
             ItemDetails itemDetails= new ItemDetails();
               itemDetails.setId(item.getId());
             itemDetails.setItemNo(item.getItemNo());
             itemDetails.setWeight(item.getWeight());
            return itemDetails;
         }).collect(Collectors.toList()));
            productDetails.setLotNo(productIn.getLotNo());
            productDetails.setProductInId(productIn.getId());
            return productDetails;
        }).collect(Collectors.toList());
    }
}
