package com.sm.user.document;

import com.sm.user.document.extention.AuditDocument;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Data
public class ProductIn extends AuditDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
            private Long id;
           private Long productId;
            private String quantity;
            private String lotNo;
            private String roomNo;
            private String currentQuantity;
            private Long customerId;
            private String session;
            private String storeId;
            private LocalDate productInDate;
            @OneToMany(mappedBy = "productIn", cascade = CascadeType.ALL, orphanRemoval = true)
            private List<Items> items;



}
