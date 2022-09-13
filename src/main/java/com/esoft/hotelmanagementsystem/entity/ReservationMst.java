package com.esoft.hotelmanagementsystem.entity;

import com.esoft.hotelmanagementsystem.enums.ReservationStatus;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
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
public class ReservationMst {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reservationId;
    private LocalDateTime promisedCheckedInTime;
    private LocalDateTime promisedCheckedOutTime;
    private LocalDateTime actualCheckedInTime;
    private LocalDateTime actualCheckedOutTime;
    private LocalDateTime createdDateTime;
    @Enumerated(EnumType.STRING)
    private ReservationStatus reservationStatus;
    private BigDecimal totalAmount;

    @ManyToMany(fetch = FetchType.EAGER)
    Set<Room> tableRooms;

    @ManyToOne(targetEntity = CustomerMst.class,fetch = FetchType.LAZY)
    @JoinColumn(name = "custId", nullable = false)
    private CustomerMst customerMst;

    @OneToOne(targetEntity = UserMst.class)
    @JoinColumn(name = "user_id")
    private UserMst createdUser;

    //credit card data
    private boolean isCreditCardApplicable;
    private String creditCardNumber;
    private LocalDate expirationDate;
    private String cardCsv;

}
