package com.sm.user.document.extention;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public abstract class AuditDocument implements Serializable {


    private String createdBy;

    private String updatedBy;

    private LocalDateTime createdDateTimeStamp;

    private LocalDateTime updatedTimeStamp=LocalDateTime.now();
}
