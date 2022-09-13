package com.esoft.hotelmanagementsystem.dto;

import com.esoft.hotelmanagementsystem.entity.RoomType;
import com.esoft.hotelmanagementsystem.enums.HouseKeepingStatus;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * @author ShanilErosh
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoomDto {

    private Long roomId;
    private String roomNumber;
    private String roomRemark;
    private HouseKeepingStatus houseKeepingStatus;
    private Boolean isNonSmoking;
    private String roomDetail;
    private BigDecimal roomPrice;
    private String roomCat;


}
