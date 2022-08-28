package com.esoft.hotelmanagementsystem.entity;

import com.esoft.hotelmanagementsystem.enums.OccupancyStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author ShanilErosh
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OccupancyMgt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long occupancyId;
    private LocalDateTime arrivalDateTime;
    private LocalDateTime depDateTime;

    @Enumerated(EnumType.STRING)
    private OccupancyStatus occupancyStatus;

    @ManyToOne(targetEntity = Room.class,fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

}
