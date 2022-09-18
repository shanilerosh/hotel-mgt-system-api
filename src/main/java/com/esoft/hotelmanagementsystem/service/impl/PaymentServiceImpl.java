package com.esoft.hotelmanagementsystem.service.impl;

import com.esoft.hotelmanagementsystem.dto.*;
import com.esoft.hotelmanagementsystem.entity.ReservationMst;
import com.esoft.hotelmanagementsystem.entity.Room;
import com.esoft.hotelmanagementsystem.entity.RoomImg;
import com.esoft.hotelmanagementsystem.entity.RoomType;
import com.esoft.hotelmanagementsystem.enums.HouseKeepingStatus;
import com.esoft.hotelmanagementsystem.exception.CommonException;
import com.esoft.hotelmanagementsystem.repo.*;
import com.esoft.hotelmanagementsystem.service.PaymentService;
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
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author ShanilErosh
 */
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    private final ReservationRepository reservationRepository;

    @Override
    public PaymentCalculatedDto getPaymentDetail(String reservationId) {

        ReservationMst reservationMst = getReservationMst(Long.valueOf(reservationId));

        //get the total amount
        LocalDateTime actualCheckedOutTime = reservationMst.getActualCheckedOutTime();
        LocalDateTime actualCheckedInTime = reservationMst.getActualCheckedInTime();

        long stayedDays = ChronoUnit.DAYS.between(actualCheckedInTime, actualCheckedOutTime);

        BigDecimal totalAmount = BigDecimal.ZERO;
        List<RoomWisePrice> roomWisePrices = new ArrayList<>();

        for (Room tableRoom : reservationMst.getTableRooms()) {
            RoomType roomType = tableRoom.getRoomType();

            RoomWisePrice roomToPrice = RoomWisePrice.builder()
                    .days(stayedDays).numberOfOccupants(roomType.getNumberOfOccupants())
                    .roomNumber(tableRoom.getRoomNumber())
                    .rowWiseAmount(roomType.getRoomPrice().multiply(BigDecimal.valueOf(stayedDays)))
                    .description(roomType.getRoomCategory().name().concat(" - " + roomType.getRoomPrice() + " X " + stayedDays))
                    .build();

            totalAmount = totalAmount.add(roomType.getRoomPrice().multiply(BigDecimal.valueOf(stayedDays)));
            roomWisePrices.add(roomToPrice);
        }

        return PaymentCalculatedDto.builder().checkedOutTime(actualCheckedOutTime).checkInTIme(actualCheckedInTime)
                .totalPayable(totalAmount).noOfDaysApplicable(stayedDays).totalPayable(totalAmount)
                .roomWisePrices(roomWisePrices).build();
    }

    private ReservationMst getReservationMst(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> {
                    throw new CommonException("Reservation Not Found");
                });
    }
}
