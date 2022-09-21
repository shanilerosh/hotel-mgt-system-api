package com.esoft.hotelmanagementsystem.repo;

import com.esoft.hotelmanagementsystem.entity.ReservationMst;
import com.esoft.hotelmanagementsystem.entity.Room;
import com.esoft.hotelmanagementsystem.entity.RoomType;
import com.esoft.hotelmanagementsystem.enums.HouseKeepingStatus;
import com.esoft.hotelmanagementsystem.enums.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author ShanilErosh
 */
public interface ReservationRepository extends JpaRepository<ReservationMst, Long> {


    List<ReservationMst> findAllByReservationStatus(ReservationStatus status);
}
