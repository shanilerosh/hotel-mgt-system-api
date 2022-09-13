package com.esoft.hotelmanagementsystem.entity;

import com.esoft.hotelmanagementsystem.enums.HouseKeepingStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Set;

/**
 * @author ShanilErosh
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomId;
    private String roomNumber;
    private String roomRemark;
    @Enumerated(EnumType.STRING)
    private HouseKeepingStatus houseKeepingStatus;
    private Boolean isNonSmoking;

    @ManyToOne(targetEntity = RoomType.class,fetch = FetchType.EAGER)
    @JoinColumn(name = "room_type_id", nullable = false)
    private RoomType roomType;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<ReservationMst> reservationMsts;

}
