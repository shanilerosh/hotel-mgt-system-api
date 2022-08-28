package com.esoft.hotelmanagementsystem.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * @author ShanilErosh
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HotelMst {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long hotelId;
    private String hotelName;
    private String hotelAddress;
    private String hotelRegistrationNumber;
    private String hotelContactNumber;

}
