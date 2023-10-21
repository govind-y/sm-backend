package com.sm.user.document;

import com.sm.user.document.extention.AuditDocument;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class ProductIn extends AuditDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
            private Long id;
    @NotNull
           private Long productId;
            @NotNull
            private String quantity;
            @NotNull
            private String lotNo;
            private String roomNo;
            private String currentQuantity;
            @NotNull
            private Long customerId;
            private String session;
            @NotNull
            private String storeId;
            private LocalDate productInDate;
//            @OneToMany(mappedBy = "productIn", cascade = CascadeType.ALL, orphanRemoval = true)
//            private List<Items> items;



}
