package com.esoft.hotelmanagementsystem.controller;

import com.esoft.hotelmanagementsystem.dto.*;
import com.esoft.hotelmanagementsystem.service.ReservationService;
import com.esoft.hotelmanagementsystem.service.RoomService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author ShanilErosh
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/reservation")
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping("/")
    @ApiImplicitParams({ @ApiImplicitParam(name = "Authorization", required = true, dataType = "string", paramType = "header") })
    public ResponseEntity<Boolean> createReservation(@RequestBody ReservationDto reservationDto) {
        return ResponseEntity.ok().body(reservationService.create(reservationDto));
    }

}
