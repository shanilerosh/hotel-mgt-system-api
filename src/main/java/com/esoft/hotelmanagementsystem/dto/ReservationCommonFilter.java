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

}
