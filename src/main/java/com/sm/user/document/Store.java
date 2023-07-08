package com.sm.user.document;

import com.sm.user.document.extention.AuditDocument;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Store extends AuditDocument {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NonNull
    private String storeName;

    private String storeId;
    @NonNull
    private String phone;
    @NonNull
    private String email;

    private String registrationSessionYear;
    @NonNull
    private Integer noOfRooms;
    private String description;
    @NonNull
    private String address;
    @NonNull
    private String registrationKey;
    @NonNull
    private String area;
    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL)
    private List<RoomDetails> roomDetails;



}
