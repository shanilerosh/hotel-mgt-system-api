package com.esoft.hotelmanagementsystem.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author ShanilErosh
 */
@Getter
@Setter
public class ReservationCommonFilter {

    //pagination related
    private int page;
    private int size;
    private String sortField;
    private String sortOrder;

    //filterations
    private LocalDateTime promisedCheckedInTimeFrom;
    private LocalDateTime promisedCheckedInTimeTo;
    private LocalDateTime promisedCheckedOutTimeFrom;
    private LocalDateTime promisedCheckedOutTimeTo;
    private LocalDateTime actualCheckedInTimeFrom;
    private LocalDateTime actualCheckedOutTimeFrom;
    private LocalDateTime actualCheckedInTimeTO;
    private LocalDateTime actualCheckedOutTimeTO;
    private String nicPass;
    private String customerName;
    private String customerCountry;
}
