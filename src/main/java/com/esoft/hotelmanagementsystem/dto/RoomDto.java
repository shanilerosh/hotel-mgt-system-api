package com.esoft.hotelmanagementsystem.dto;

import com.esoft.hotelmanagementsystem.entity.RoomType;
import com.esoft.hotelmanagementsystem.enums.HouseKeepingStatus;
import lombok.*;

import javax.persistence.*;

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

}
