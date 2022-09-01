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
public class RoomDataDto implements Serializable {
    private Long roomTypeId;
    private String roomDetail;
    private String cat;
    private BigDecimal roomPrice;
    private String mainImg;
    private BigDecimal numberOfOccupants;

    private List<String> subImages;
    private List<RoomDto> roomDtos;
}
