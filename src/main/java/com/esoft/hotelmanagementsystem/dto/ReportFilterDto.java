package com.esoft.hotelmanagementsystem.dto;

import com.esoft.hotelmanagementsystem.enums.ReportType;
import com.esoft.hotelmanagementsystem.enums.ReservationStatus;
import lombok.*;

import javax.validation.constraints.NotNull;
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
    @NotNull(message = "Report type is compulsory")
    private ReportType reportType;
}
