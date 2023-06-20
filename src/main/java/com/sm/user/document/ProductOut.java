package com.sm.user.document;

import com.sm.user.document.extention.AuditDocument;
import com.sm.user.document.extention.Product;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class ProductOut extends AuditDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
         private Long id;
           private String productNumber;
            private String lotNo;
            private String quantity;
            @ManyToOne
            @JoinColumn(name = "product_id")
           private Product product;
            private String reasonOfOut;
            private String soldToBossinessMan;

}
