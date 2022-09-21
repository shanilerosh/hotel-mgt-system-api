package com.esoft.hotelmanagementsystem.dto;

import com.esoft.hotelmanagementsystem.enums.ReservationStatus;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author ShanilErosh
 */

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ReportFilterDto implements Serializable {

    private LocalDateTime from;
    private LocalDateTime to;
    private ReservationStatus reservationStatus;
}
