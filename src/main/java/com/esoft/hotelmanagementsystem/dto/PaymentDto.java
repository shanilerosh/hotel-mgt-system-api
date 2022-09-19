package com.esoft.hotelmanagementsystem.dto;

import com.esoft.hotelmanagementsystem.enums.PaymentType;
import lombok.*;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
    private BigDecimal paymentAmount;
    private Boolean isManualPayment;
    private LocalDateTime paymentDateTime;
    private String reservationId;
}
