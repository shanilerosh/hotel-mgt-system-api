package com.esoft.hotelmanagementsystem.dto;

import com.esoft.hotelmanagementsystem.enums.ReservationStatus;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author ShanilErosh
 */

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ReservationDto implements Serializable {

    private Long reservationId;
    private LocalDateTime actualCheckedInTime;
    private LocalDateTime actualCheckedOutTime;
    private LocalDateTime createdDateTime;
    private ReservationStatus reservationStatus;
    private BigDecimal totalAmount;

    private List<RoomDto> roomList;

}
