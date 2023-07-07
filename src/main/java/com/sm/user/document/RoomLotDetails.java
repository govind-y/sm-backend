package com.sm.user.document;

import com.sm.user.document.extention.AuditDocument;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
@Entity
@Data
public class RoomLotDetails extends AuditDocument {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String storeId;
    private String generatedLotName;
    private Integer lotCapacity;
    private Integer floorNo;
    private String roomId;
    private String roomNo;
    private Integer currentLotCapacity;
    private String session;
    private String status;
    private String allocatedCustomer;
}
