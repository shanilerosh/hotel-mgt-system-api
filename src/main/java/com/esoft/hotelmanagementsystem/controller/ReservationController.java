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
     * APi To fetch reservation by Id
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiImplicitParams({ @ApiImplicitParam(name = "Authorization", required = true, dataType = "string", paramType = "header") })
    public ResponseEntity<ReservationDto> fetchReservationById(@PathVariable String id) {
        return ResponseEntity.ok().body(reservationService.fetchOne(Long.valueOf(id)));
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
    @PostMapping("/clark/{status}")
    @ApiImplicitParams({ @ApiImplicitParam(name = "Authorization", required = true, dataType = "string", paramType = "header") })
    public ResponseEntity<CommonResponseDto<ReservationDto>> fetchReservationData(@RequestBody ReservationCommonFilter reservationDto,
                                                                                  @PathVariable String status) {
        return ResponseEntity.ok().body(reservationService.fetchReservationData(reservationDto, status, true));
    }

    /**
     * API to check in a reservation
     * @param reservationModifyDto
     * @return
     */
    @PostMapping("/check-in")
    @ApiImplicitParams({ @ApiImplicitParam(name = "Authorization", required = true, dataType = "string", paramType = "header") })
    public ResponseEntity<Boolean> checkInReservation(@RequestBody ReservationModifyDto reservationModifyDto) {
        return ResponseEntity.ok().body(reservationService.checkInReservation(reservationModifyDto));
    }


    /**
     * API to check out a reservation
     * @param reservationModifyDto
     * @return
     */
    @PostMapping("/check-out")
    @ApiImplicitParams({ @ApiImplicitParam(name = "Authorization", required = true, dataType = "string", paramType = "header") })
    public ResponseEntity<Boolean> checkOutReservation(@RequestBody ReservationModifyDto reservationModifyDto) {
        return ResponseEntity.ok().body(reservationService.checkOutReservation(reservationModifyDto));
    }

    /**
     * API to cancel reservation
     * @param reservationCancelDto
     * @return
     */
    @PostMapping("/cancel")
    @ApiImplicitParams({ @ApiImplicitParam(name = "Authorization", required = true, dataType = "string", paramType = "header") })
    public ResponseEntity<Boolean> cancelReservation(@RequestBody ReservationCancelDto reservationCancelDto) {
        return ResponseEntity.ok().body(reservationService.cancelReservation(reservationCancelDto));
    }

    /**
     * API to Update credit card details
     *
     * @return
     */
    @PostMapping("/credit-card/{reservationId}")
    @ApiImplicitParams({ @ApiImplicitParam(name = "Authorization", required = true, dataType = "string", paramType = "header") })
    public ResponseEntity<Boolean> updateCreditCardDetail(@PathVariable String reservationId, @RequestBody CreditCardDto creditCardDto) {
        return ResponseEntity.ok().body(reservationService.updateCreditCardDetail(creditCardDto, reservationId));
    }


}
