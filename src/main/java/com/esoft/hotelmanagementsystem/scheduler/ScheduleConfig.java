package com.esoft.hotelmanagementsystem.scheduler;

import com.esoft.hotelmanagementsystem.enums.ReservationStatus;
import com.esoft.hotelmanagementsystem.repo.ReservationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;

/**
 * @author ShanilErosh
 */
@Configuration
@EnableScheduling
@RequiredArgsConstructor
@Slf4j
public class ScheduleConfig {


    private final ReservationRepository reservationRepository;


    /**
     * Method to Ran Daily at 7 PM to cancel the reservation created without credit card
     */
    @Scheduled(cron = "0 0 7 * * ?")
    public void cancelCreditCardNonProvidedReservations() {

        try {
            log.info("Credit card cancellation sync started on ".concat(LocalDateTime.now().toString()));

            reservationRepository.findAllByReservationStatus(ReservationStatus.OPEN).forEach(obj -> {
                obj.setReservationStatus(ReservationStatus.CANCELED);
                obj.setCancalationReason("No Credit card provided. System automatically cancelled the reservation");
                reservationRepository.save(obj);
            });

            log.info("Credit card cancellation sync ended on ".concat(LocalDateTime.now().toString()));
        } catch (Exception exception) {
            log.error("Credit card cancellation sync error occured on ".concat(LocalDateTime.now().toString()), exception.getCause());
        }

    }
}
