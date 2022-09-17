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
public class ReservationCancelDto {

    @NotNull(message = "Reservation Id is mandatory")
    private Long resevationId;
    private String cancellationReason;
}
