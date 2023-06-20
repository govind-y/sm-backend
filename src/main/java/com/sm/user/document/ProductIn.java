package com.sm.user.document;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
@Entity
@Data
public class ProductIn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
            private Long id;
            private String productNumber;
            private String productName;
            private String product_type;
            private String quality_name ;
            private String quantity;
            private String lotNo;
            private String roomNo;
            private String currentQuantity;
            private String customerNumber;
            private String session;
            private String storeId;

}
