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
    @ManyToOne
    @JoinColumn(name = "product_out_id")
    private ProductOut productOut;
    @ManyToOne
    @JoinColumn(name = "product_in_id")
    private ProductIn productIn;


}
