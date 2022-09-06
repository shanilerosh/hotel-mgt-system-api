package com.esoft.hotelmanagementsystem.entity;

import com.esoft.hotelmanagementsystem.enums.PaymentType;
import com.esoft.hotelmanagementsystem.enums.ReservationStatus;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * @author ShanilErosh
 */
@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentMst {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;
    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;
    private BigDecimal paymentAmount;
    private Boolean isManualPayment;
    private LocalDateTime paymentDateTime;

    @ManyToOne(targetEntity = ReservationMst.class,fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id", nullable = false)
    private ReservationMst reservationMst;

}
