package com.esoft.hotelmanagementsystem.repo;

import com.esoft.hotelmanagementsystem.entity.PaymentMst;
import com.esoft.hotelmanagementsystem.entity.ReservationMst;
import com.esoft.hotelmanagementsystem.entity.Role;
import com.esoft.hotelmanagementsystem.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author ShanilErosh
 */
public interface PaymentRepository extends JpaRepository<PaymentMst, Long> {

    Optional<PaymentMst> findByReservationMstAndPaymentStatus(ReservationMst reservationMst, PaymentStatus paymentStatus);
}
