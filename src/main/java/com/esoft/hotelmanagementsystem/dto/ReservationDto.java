package com.esoft.hotelmanagementsystem.dto;

import com.esoft.hotelmanagementsystem.enums.ReservationStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
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
    private LocalDateTime promisedCheckedInTime;
    private LocalDateTime promisedCheckedOutTime;
    private LocalDateTime actualCheckedInTime;
    private LocalDateTime actualCheckedOutTime;
    private LocalDateTime createdDateTime;
    private ReservationStatus reservationStatus;
    private BigDecimal totalAmount;

    private List<RoomDto> roomList;

    //user related
    private String username;

    ///customer dto
    private CustomerDto customerDto;

    //creadit card data
    @JsonProperty("isCreditCardApplicable")
    private boolean isCreditCardApplicable;
    private String creditCardNumber;
    private LocalDate expirationDate;
    private String cardCsv;

    private String customerName;
    private String country;
    private String nicPass;
    private String status;

    private String cancalationReason;

    //Payment Related Data
    private String paymentType;
    private BigDecimal paymentAmount;
    private LocalDateTime paymentDate;



}
