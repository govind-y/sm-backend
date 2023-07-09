package com.sm.user.document;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;

@Entity
@Data
public class ProductIn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
            private Long id;
           private Long productId;
            private String quantity;
            private String lotNo;
            private String roomNo;
            private String currentQuantity;
            private String customerNumber;
            private String session;
            private String storeId;
            private LocalDate productInDate;


}
