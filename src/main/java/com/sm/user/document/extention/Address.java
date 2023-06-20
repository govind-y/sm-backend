package com.sm.user.document.extention;

import com.sm.user.document.Customer;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String village;
    private String city;
    private String state;
    private String zipcode;
    private String country;
    private String addressLine1;
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
}
