package com.sm.user.document;

import com.sm.user.document.extention.AuditDocument;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;

@Entity
@Data
public class LotSoldSchedule extends AuditDocument {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long customerId;
    private String storeId;
    private String session;
    private String lotNo;
    private Long soldQuantity;
    private Long soldOutQuantity;
    private Double price;
    private String soldStatus;
    private String soldType;//full, partial
    private LocalDate soldDate;
    private LocalDate weightDate;
    private LocalDate paymentDate;


}
