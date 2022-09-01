package com.esoft.hotelmanagementsystem.dto;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * @author ShanilErosh
 */

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class HotelDto implements Serializable {

    private Long hotelId;
    private String hotelName;
    private String hotelAddress;
    private String hotelRegistrationNumber;
    private String hotelContactNumber;

}
