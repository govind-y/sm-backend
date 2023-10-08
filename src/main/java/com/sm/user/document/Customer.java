package com.sm.user.document;

import com.sm.user.document.extention.Address;
import com.sm.user.document.extention.AuditDocument;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.util.List;

@Data
//@Document(collection = MongoCollection.CUSTOMER)
@Entity
@NoArgsConstructor
public class Customer  extends AuditDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String middleName;
    @NotNull
    private String phone;
    @NotNull
    private String email;
    private String vehicleNumber;
    @NotNull
    private String customerType;
    private String storeId;
    private String registerSession;
    private String roleType;
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<Address> addresses;


}
