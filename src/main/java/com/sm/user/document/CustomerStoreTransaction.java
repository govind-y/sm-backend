package com.sm.user.document;

import com.sm.user.document.extention.AuditDocument;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Data
@Entity
public class CustomerStoreTransaction extends AuditDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String customerNumber;
    private Double storeCharge;
    private LocalDateTime chargeStartDate;
    private LocalDateTime chargeEndDate;
    private String chargeType;
    private Double amount;
    private Integer quantity;
    private Integer rate;
    private String description;
    private Integer interestRate;
    private String lotNo;
    private String storeId;
}
