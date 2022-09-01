package com.esoft.hotelmanagementsystem.dto;

import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author ShanilErosh
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoomDataDto implements Serializable {
    private Long roomTypeId;
    private String roomDetail;
    private String cat;
    private BigDecimal roomPrice;
    private BigDecimal mainImg;
    private BigDecimal numberOfOccupants;
}
