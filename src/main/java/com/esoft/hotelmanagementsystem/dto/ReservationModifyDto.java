package com.esoft.hotelmanagementsystem.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author ShanilErosh
 */
@Getter
@Setter
public class ReservationModifyDto {

    private LocalDateTime applDateTime;
    private String resevationId;
}
