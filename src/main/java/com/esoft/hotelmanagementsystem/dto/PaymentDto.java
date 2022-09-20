package com.esoft.hotelmanagementsystem.dto;

import com.esoft.hotelmanagementsystem.enums.PaymentType;
import lombok.*;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author ShanilErosh
 */

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PaymentDto implements Serializable {

    private Long paymentId;
    private PaymentType paymentType;
    @NotNull(message = "Payment amount is compulsory")
    private BigDecimal paymentAmount;
    private Boolean isManualPayment;
    private LocalDateTime paymentDateTime;
    @NotNull(message = "Reservation Id is compulsory")
    private String reservationId;

    private BigDecimal laundryCharges;
    private BigDecimal barCharges;
    private BigDecimal telephoneCharges;
    private BigDecimal clubFacility;
    private BigDecimal ketCharges;
}
