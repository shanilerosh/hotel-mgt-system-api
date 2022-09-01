package com.esoft.hotelmanagementsystem.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author ShanilErosh
 */
@Getter
@Setter
public class HotelMgtCommonFilter {

    //pagination related
    private int page;
    private int size;
    private String sortField;
    private String sortOrder;

    //filterations
    private String roomCategory;
    private int numberOfOccupants;
    private String hotelType;
    private LocalDateTime arrivalTime;
    private LocalDateTime departureDateTime;

}
