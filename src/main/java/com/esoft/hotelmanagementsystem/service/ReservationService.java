package com.esoft.hotelmanagementsystem.service;

import com.esoft.hotelmanagementsystem.dto.*;

/**
 * @author ShanilErosh
 */
public interface ReservationService extends CrudService<ReservationDto, Long>{


    Boolean createClarkReservation(ReservationDto reservationDto);

    CommonResponseDto<ReservationDto> fetchReservationData(ReservationCommonFilter reservationDto, String status, boolean isClark);

    Boolean checkInReservation(ReservationModifyDto reservationModifyDto);

    Boolean checkOutReservation(ReservationModifyDto reservationModifyDto);

    Boolean cancelReservation(ReservationCancelDto reservationCancelDto);

    Boolean updateCreditCardDetail(CreditCardDto creditCardDto, String reservationId);
}
