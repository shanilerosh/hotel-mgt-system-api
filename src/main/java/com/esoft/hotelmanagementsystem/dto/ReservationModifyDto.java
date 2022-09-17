package com.esoft.hotelmanagementsystem.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * @author ShanilErosh
 */
@Getter
@Setter
public class ReservationModifyDto {

    @NotNull(message = "Applicable date time is mandoatory")
    private LocalDateTime applDateTime;
    @NotNull(message = "Reservation Id is mandatory")
    private String resevationId;
}
