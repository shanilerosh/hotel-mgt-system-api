package com.esoft.hotelmanagementsystem.dto;

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
public class PaymentCalculatedDto implements Serializable {

    private LocalDateTime checkInTIme;
    private LocalDateTime checkedOutTime;
    private BigDecimal totalPayable;
    private long noOfDaysApplicable;

    private List<RoomWisePrice> roomWisePrices;

    private BigDecimal laundryCharges;
    private BigDecimal barCharges;
    private BigDecimal telephoneCharges;
    private BigDecimal clubFacility;
    private BigDecimal ketCharges;
}
