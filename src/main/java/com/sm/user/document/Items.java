package com.sm.user.document;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Items {
@Id
@GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Integer weight;
    private Integer itemNo;
    private String lotNo;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private Long productOutId;
//    @ManyToOne
//    @JoinColumn(name = "product_in_id")
    private Long productInId;
    private Long lotScheduleId;


}
