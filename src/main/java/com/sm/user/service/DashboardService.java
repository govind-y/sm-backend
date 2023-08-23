package com.sm.user.service;

import com.sm.user.document.Customer;
import com.sm.user.document.CustomerLoan;
import com.sm.user.document.Items;
import com.sm.user.document.ProductIn;
import com.sm.user.document.dto.CustomerTransactionResponse;
import com.sm.user.document.dto.DashboardCountResponse;
import com.sm.user.document.dto.DashboardCustomerResponse;
import com.sm.user.repository.CustomerLoanRepository;
import com.sm.user.repository.CustomerRepository;
import com.sm.user.repository.ItemsRepository;
import com.sm.user.repository.ProductInRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.persistence.Tuple;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class DashboardService {
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private ProductInRepository productInRepository;
    @Autowired
    private ItemsRepository itemsRepository;
    @Autowired
    private CustomerLoanRepository  customerLoanRepository;



    public DashboardCountResponse getDashboardCount(String sessionYear) {
        Tuple ss = customerRepository.getCountOfDashboardData(sessionYear);
        DashboardCountResponse countResponse = new DashboardCountResponse(
                ss.get(0, BigInteger.class),
                ss.get(1, Double.class),
                ss.get(2, Double.class),
                ss.get(3, Double.class)
        );
        return countResponse;

    }

    public Double getProductInDetailByBrandType(String brandType) {

        return customerRepository.totalAvailableQualityProductIn(brandType);

    }

    public Double getProductOutDetailByBrandType(String brandType) {

        return customerRepository.totalAvailableQualityProductOut(brandType);

    }

    public Double getProductInDetailByRoomNo(String roomNo) {

        return customerRepository.totalAvailableQualityProductInByRoom(roomNo);

    }

    public Double getProductOutDetailByRoomNo(String roomNO) {

        return customerRepository.totalAvailableQualityProductOutByRoom(roomNO);

    }

    public List<DashboardCustomerResponse> getDashboardResponse(String session, Long averageRate, Long storeCharge) {
        List<Customer> customers = customerRepository.findAll();
        List<ProductIn> productIns = productInRepository.findAllBySessionEquals(session);

        Map<Long, List<ProductIn>> productInMap = productIns.stream().filter(item -> item.getLotNo() != null && item.getCustomerId() != null).collect(Collectors.groupingBy(ProductIn::getCustomerId));
       List<DashboardCustomerResponse> responses= new ArrayList<>();
        for (Map.Entry<Long, List<ProductIn>> map : productInMap.entrySet()) {
            DashboardCustomerResponse response = new DashboardCustomerResponse();
//            List<Items> outItems = itemsRepository.findAllByproductInIdAndOutIdNotNull(map.getValue().stream().map(ProductIn::getProductId).collect(Collectors.toList()));
            Long availableProduct = map.getValue().stream().mapToLong(value -> Long.valueOf(value.getCurrentQuantity())).sum();
            response.setProductCount(availableProduct);
            response.setAverageValueOfProduct(availableProduct * (averageRate-storeCharge));
            List<CustomerLoan> customerLoans = customerLoanRepository.findAllByCustomerId(map.getKey());
            if(CollectionUtils.isEmpty(customerLoans)){
                double debitLoan = customerLoans.stream().filter(customerLoan -> customerLoan.getLoanType().equalsIgnoreCase("DEBIT")).mapToDouble(customerLoan -> customerLoan.getAmount()).sum();
                double creditLoan = customerLoans.stream().filter(customerLoan -> customerLoan.getLoanType().equalsIgnoreCase("CREDIT")).mapToDouble(customerLoan -> customerLoan.getAmount()).sum();
                response.setTotalLoanAmount(CollectionUtils.isEmpty(customerLoans)?0l:debitLoan-creditLoan);
                response.setCustomerType(availableProduct * (averageRate-storeCharge)-(debitLoan-creditLoan)>0?"GOOD":"DEFAULTER");
            }


            customers.stream().filter(customer -> customer.getId() == map.getKey()).findFirst().ifPresent(cust -> {
                response.setCustomerName(cust.getFirstName() + " " + (cust.getLastName() != null ? cust.getLastName() : ""));
            });

            responses.add(response);
        }

return responses;
    }


}
