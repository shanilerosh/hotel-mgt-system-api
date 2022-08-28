package com.esoft.hotelmanagementsystem.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * @author ShanilErosh
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomTypeId;
    private String roomDetail;
    private Long numberOfOccupants;
    private BigDecimal roomPrice;

    @ManyToOne(targetEntity = HotelMst.class,fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_id", nullable = false)
    private HotelMst hotelMst;

}
