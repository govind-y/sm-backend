package com.sm.user.document;

import com.sm.user.document.extention.AuditDocument;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class ProductOut extends AuditDocument {

            @Id
            @GeneratedValue(strategy = GenerationType.IDENTITY)
            private Long id;
            private String lotNo;
            private String quantity;
            private String reasonOfOut;
            private Long  customerId;
            private Long  soldBussinessManId;
            private String session;


}
