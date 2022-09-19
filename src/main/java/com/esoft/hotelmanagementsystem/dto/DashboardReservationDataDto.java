package com.esoft.hotelmanagementsystem.dto;

import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class DashboardReservationDataDto implements Serializable {
    private Integer reservationId;
    private String customerName;
    private String contactNo;
    private LocalDateTime promisedCheckInOutDateTime;

}
