package com.esoft.hotelmanagementsystem.dto;

import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class DashboardDto implements Serializable {
    private BigDecimal totalRevenueToday;
    private Integer totalCheckinsToday;
    private Integer newReservationsToday;
    private Integer dueCheckinsToday;
    private Integer availableRooms;
    private Integer totalActiveGuests;
    private List<DashboardReservationDataDto> expectedCheckInListToday;
    private List<DashboardReservationDataDto> expectedCheckOutListToday;
}
