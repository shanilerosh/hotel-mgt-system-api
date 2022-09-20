package com.esoft.hotelmanagementsystem.service.impl;

import com.esoft.hotelmanagementsystem.dto.*;
import com.esoft.hotelmanagementsystem.entity.*;
import com.esoft.hotelmanagementsystem.enums.HouseKeepingStatus;
import com.esoft.hotelmanagementsystem.enums.PaymentStatus;
import com.esoft.hotelmanagementsystem.enums.PaymentType;
import com.esoft.hotelmanagementsystem.enums.ReservationStatus;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final PaymentRepository paymentRepository;

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

    /**
     * Create cash payment invoice record in the db
     * @param paymentDto
     * @return
     */
    @Override
    public Boolean createPaymentInvoiceRecord(PaymentDto paymentDto) {

        ReservationMst reservationMst = getReservationMst(Long.valueOf(paymentDto.getReservationId()));

        //validate duplicates
        paymentRepository.findByReservationMstAndPaymentStatus(reservationMst, PaymentStatus.SUCCESS)
                .ifPresent(c-> {
                    throw new CommonException("A successful payment has been already created for this reservation. Please try again");
                });

        PaymentMst paymentMst = paymentMstBuilder();

        BeanUtils.copyProperties(paymentDto, paymentMst,"paymentType","paymentDateTime");

        paymentMst.setPaymentType(paymentDto.getPaymentType());
        paymentMst.setReservationMst(reservationMst);

        paymentRepository.save(paymentMst);

        return true;
    }

    @Override
    public Boolean finalizePaymentStatus(String reservationId, String paymentId, String payerId, PaymentStatus paymentStatus) {

        ReservationMst reservationMst = getReservationMst(Long.valueOf(reservationId));

        PaymentMst paymentMst = paymentRepository.findByReservationMstAndPaymentStatus(reservationMst, PaymentStatus.OPEN)
                .orElseThrow(() -> {
                    throw new CommonException("Payment Record not found please try again");
                });

        paymentMst.setPaypalPayerId(payerId);
        paymentMst.setPaypalPaymentId(paymentId);

        paymentMst.setPaymentStatus(paymentStatus);

        paymentRepository.save(paymentMst);

        reservationMst.setReservationStatus(ReservationStatus.COMPLETED);

        reservationRepository.save(reservationMst);

        return true;
    }

    @Override
    public Boolean createCashPayment(PaymentDto paymentDto) {


        paymentDto.setPaymentType(PaymentType.CASH);
        paymentDto.setIsManualPayment(true);

        createPaymentInvoiceRecord(paymentDto);

        //finalize payment
        finalizePaymentStatus(paymentDto.getReservationId(),null, null, PaymentStatus.SUCCESS);

        return true;
    }

    private PaymentMst paymentMstBuilder() {
        //Todo -Add logged in user data
        return PaymentMst.builder().paymentDateTime(LocalDateTime.now())
                .paymentStatus(PaymentStatus.OPEN)
                .build();
    }

    private ReservationMst getReservationMst(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> {
                    throw new CommonException("Reservation Not Found");
                });
    }
}
