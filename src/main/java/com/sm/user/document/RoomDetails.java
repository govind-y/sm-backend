package com.sm.user.document;

import com.sm.user.document.Store;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
public class RoomDetails implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String roomId;
    private Integer floorInRoom;
    private Integer columnInRoom;
    private String roomCapacity;
    private String roomNo;
    private String description;
    private Integer perLotCapacity;
    @ManyToOne
    @JoinColumn(name = "store_pk_id")
    private Store store;
}
