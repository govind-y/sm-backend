package com.sm.user.document;

import com.sm.user.document.extention.Address;
import com.sm.user.document.extention.AuditDocument;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
//@Document(collection = MongoCollection.CUSTOMER)
@Entity
public class Customer  extends AuditDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String customerNumber;
    private String firstName;
    private String lastName;
    private String middleName;
    private String phone;
    private String email;
    private String vehicleNumber;
    private String customerType;
    private String storeId;
    private String registerSession;
    private String roleType;
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<Address> addresses;


}
