package com.sm.user.transformer;

import com.sm.user.document.Customer;
import com.sm.user.document.CustomerStoreTransaction;
import com.sm.user.document.dto.CustomerTransactionResponse;
import lombok.Data;
import org.dozer.DozerBeanMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Data
@Service
public class CustomerTransformer {

    public Customer convertCustomer(Customer customer){

        return customer;
    }

    public List<CustomerTransactionResponse> customerTransactionTransformer(Optional<Customer> customer, List<CustomerStoreTransaction> transactions) {
      return   transactions.stream().map(transaction->{
            CustomerTransactionResponse response = new DozerBeanMapper().map(transaction, CustomerTransactionResponse.class);
            if(customer.isPresent()){
                response.setCustomerName(customer.isPresent()?customer.get().getFirstName()+" "+customer.get().getLastName():"");
                response.setCustomerPhone(customer.get().getPhone());
            }

            return response;
        }).collect(Collectors.toList());

    }
}
