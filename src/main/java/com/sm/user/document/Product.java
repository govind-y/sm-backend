package com.sm.user.document;

import com.sm.user.document.ProductOut;
import jdk.dynalink.linker.LinkerServices;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String productId;
    private String productType;
    private String productWeight;
    private String productSize;


}
