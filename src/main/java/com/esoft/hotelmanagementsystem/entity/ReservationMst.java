package com.esoft.hotelmanagementsystem.entity;

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
public class ReservationMst {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reservationId;
    private LocalDateTime actualCheckedInTime;
    private LocalDateTime actualCheckedOutTime;
    private LocalDateTime createdDateTime;
    private ReservationStatus reservationStatus;
    private BigDecimal totalAmount;

    @ManyToMany
    Set<Room> tableRooms;

    //TODO - Integrate customer

}
