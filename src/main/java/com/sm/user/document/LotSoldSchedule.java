package com.sm.user.document;

import com.sm.user.document.dto.ItemDetails;
import com.sm.user.document.extention.AuditDocument;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Data
public class LotSoldSchedule extends AuditDocument {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long customerId;
    private Long supplierId;
    private String storeId;
    private String session;
    private String lotNo;
    private Long soldQuantity;
    private Double price;
    private String soldStatus; // IN_PROGRESS, WEIGHT_DONE, PAYMENT_SETTLED, REJECT,PAID,
    private String soldType;//full, partial
    private LocalDate soldDate;
    private LocalDate weightDate;
    private LocalDate paymentDate;
    private Long totalAmount;
    private Long paidAmount;
    private Long storeCharge;
    private Double weightCharge;
    @Transient
    private String customerName;
    @Transient
    private String supplierName;
    @Transient
    private Long weightedCount;
    @Transient
    private List<ItemDetails> itemDetails;



}
