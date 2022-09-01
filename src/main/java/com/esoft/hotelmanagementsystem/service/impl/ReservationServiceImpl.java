package com.esoft.hotelmanagementsystem.service.impl;

import com.esoft.hotelmanagementsystem.dto.*;
import com.esoft.hotelmanagementsystem.entity.ReservationMst;
import com.esoft.hotelmanagementsystem.entity.Room;
import com.esoft.hotelmanagementsystem.entity.RoomImg;
import com.esoft.hotelmanagementsystem.entity.RoomType;
import com.esoft.hotelmanagementsystem.enums.HouseKeepingStatus;
import com.esoft.hotelmanagementsystem.enums.ReservationStatus;
import com.esoft.hotelmanagementsystem.exception.CommonException;
import com.esoft.hotelmanagementsystem.repo.*;
import com.esoft.hotelmanagementsystem.service.ReservationService;
import com.esoft.hotelmanagementsystem.service.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author ShanilErosh
 */
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ReservationServiceImpl implements ReservationService {

    private final RepositoryCustom customRepo;
    private final HotelTypeRepo hotelTypeRepo;
    private final RoomTypeRepo roomTypeRepo;
    private final RoomRepository roomRepo;
    private final RoomImgRepository roomImgRepo;
    private final ReservationRepository reservationRepository;


    @Override
    public Boolean create(ReservationDto dto) {

        var reservationMst = reservationBuilder();
        BeanUtils.copyProperties(dto, reservationMst);

        try {
            var savedReservation = reservationRepository.save(reservationMst);

            //save room related tables
            Set<Room> rooms = dto.getRoomList().stream().map(obj -> roomRepo.findById(obj.getRoomId()).orElseThrow(() -> {
                throw new CommonException("Room not found");
            })).collect(Collectors.toSet());

            savedReservation.setTableRooms(rooms);

            reservationRepository.save(savedReservation);
            return true;
        } catch (Exception exception) {
            log.error(exception.getMessage(), exception);
            throw new CommonException(exception.getMessage());
        }
    }

    private ReservationMst reservationBuilder() {
        ReservationMst reservation = ReservationMst.builder().build();
        reservation.setCreatedDateTime(LocalDateTime.now());
        reservation.setReservationStatus(ReservationStatus.OPEN);

        return reservation;
    }

    @Override
    public Boolean update(Long aLong, ReservationDto dto) {
        return null;
    }

    @Override
    public ReservationDto fetchOne(Long aLong, ReservationDto dto) {
        return null;
    }
}
