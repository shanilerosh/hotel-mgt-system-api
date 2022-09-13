package com.esoft.hotelmanagementsystem.service;

import com.esoft.hotelmanagementsystem.dto.*;

import java.util.List;

/**
 * @author ShanilErosh
 */
public interface ReservationService extends CrudService<ReservationDto, Long>{


    Boolean createClarkReservation(ReservationDto reservationDto);

    CommonResponseDto<ReservationDto> fetchReservationData(ReservationCommonFilter reservationDto, String status, boolean isClark);

    Boolean checkInReservation(ReservationModifyDto reservationModifyDto);

    Boolean checkOutReservation(ReservationModifyDto reservationModifyDto);
}
