package com.esoft.hotelmanagementsystem.dto;

import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author ShanilErosh
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoomWisePrice implements Serializable {
    private String roomNumber;
    private long numberOfOccupants;
    private long days;
    private String description;
    private BigDecimal rowWiseAmount;

}
