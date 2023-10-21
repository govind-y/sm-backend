package com.sm.user.document;

import com.sm.user.document.extention.AuditDocument;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;

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
//    @NotNull
    private String customerType;
    private String storeId;
    private String registerSession;
    private String roleType;
        private String address;


}
