package com.esoft.hotelmanagementsystem.controller;

import com.esoft.hotelmanagementsystem.dto.CommonResponseDto;
import com.esoft.hotelmanagementsystem.dto.HotelDto;
import com.esoft.hotelmanagementsystem.dto.HotelMgtCommonFilter;
import com.esoft.hotelmanagementsystem.dto.RoomDataDto;
import com.esoft.hotelmanagementsystem.entity.Role;
import com.esoft.hotelmanagementsystem.entity.UserMst;
import com.esoft.hotelmanagementsystem.service.RoomService;
import com.esoft.hotelmanagementsystem.service.UserService;
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
@RequestMapping("/api/rooms")
public class RoomController {

    private final RoomService roomService;

    @PostMapping("/room-type")
    @ApiImplicitParams({ @ApiImplicitParam(name = "Authorization", required = true, dataType = "string", paramType = "header") })
    public ResponseEntity<CommonResponseDto<RoomDataDto>> fetchFilteredRooms(@RequestBody HotelMgtCommonFilter hotelMgtCommonFilter) {
        return ResponseEntity.ok().body(roomService.fetch(hotelMgtCommonFilter));
    }

    @GetMapping("/hotel-type")
    @ApiImplicitParams({ @ApiImplicitParam(name = "Authorization", required = true, dataType = "string", paramType = "header") })
    public ResponseEntity<List<HotelDto>> fetchHotelType() {
        return ResponseEntity.ok().body(roomService.fetchHotelType());
    }



}
