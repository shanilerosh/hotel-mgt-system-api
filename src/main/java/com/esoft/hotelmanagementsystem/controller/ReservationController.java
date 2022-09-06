package com.esoft.hotelmanagementsystem.controller;

import com.esoft.hotelmanagementsystem.dto.*;
import com.esoft.hotelmanagementsystem.service.ReservationService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author ShanilErosh
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/reservation")
public class ReservationController {

    private final ReservationService reservationService;

    /**
     * API to create a reservation as a customer
     * @param reservationDto
     * @return
     */
    @PostMapping("/")
    @ApiImplicitParams({ @ApiImplicitParam(name = "Authorization", required = true, dataType = "string", paramType = "header") })
    public ResponseEntity<Boolean> createReservation(@RequestBody ReservationDto reservationDto) {
        return ResponseEntity.ok().body(reservationService.createReservation(reservationDto));
    }


    /**
     * API to create reservation as a clark
     * @param reservationDto
     * @return
     */
    @PostMapping("/clark/")
    @ApiImplicitParams({ @ApiImplicitParam(name = "Authorization", required = true, dataType = "string", paramType = "header") })
    public ResponseEntity<Boolean> createReservationAsClark(@RequestBody ReservationDto reservationDto) {
        return ResponseEntity.ok().body(reservationService.createClarkReservation(reservationDto));
    }


    /**
     * Fetch Reservation Data by status
     * @param reservationDto
     * @return
     */
    @PostMapping("/{status}")
    @ApiImplicitParams({ @ApiImplicitParam(name = "Authorization", required = true, dataType = "string", paramType = "header") })
    public ResponseEntity<CommonResponseDto<ReservationDto>> fetchReservationData(@RequestBody ReservationCommonFilter reservationDto,
                                                                                  @PathVariable String status) {
        return ResponseEntity.ok().body(reservationService.fetchReservationData(reservationDto, status));
    }

}
