package com.esoft.hotelmanagementsystem.service.impl;

import com.esoft.hotelmanagementsystem.dto.DashboardDto;
import com.esoft.hotelmanagementsystem.dto.DashboardReservationDataDto;
import com.esoft.hotelmanagementsystem.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {
    @Override
    public DashboardDto fetchDashboardData() {
        DashboardDto dashboardDto = new DashboardDto();

        dashboardDto.setTotalRevenueToday(new BigDecimal(180000));
        dashboardDto.setTotalCheckinsToday(18);
        dashboardDto.setNewReservationsToday(10);
        dashboardDto.setDueCheckinsToday(7);
        dashboardDto.setAvailableRooms(45);
        dashboardDto.setTotalActiveGuests(200);

        List<DashboardReservationDataDto> checkInList = new ArrayList<>();
        List<DashboardReservationDataDto> checkOutList = new ArrayList<>();

        DashboardReservationDataDto arrivalone = new DashboardReservationDataDto();
        arrivalone.setReservationId(1);
        arrivalone.setCustomerName("abc alwis");
        arrivalone.setContactNo("0774585695");
        arrivalone.setPromisedCheckInOutDateTime(LocalDateTime.now());

        DashboardReservationDataDto arrivaltwo = new DashboardReservationDataDto();
        arrivaltwo.setReservationId(1);
        arrivaltwo.setCustomerName("basil");
        arrivaltwo.setContactNo("0774585695");
        arrivaltwo.setPromisedCheckInOutDateTime(LocalDateTime.now());

        DashboardReservationDataDto arrivalthree = new DashboardReservationDataDto();
        arrivalthree.setReservationId(1);
        arrivalthree.setCustomerName("Pasan silva");
        arrivalthree.setContactNo("0774585695");
        arrivalthree.setPromisedCheckInOutDateTime(LocalDateTime.now());
        checkInList.add(arrivalone);
        checkInList.add(arrivaltwo);
        checkInList.add(arrivalthree);

        DashboardReservationDataDto depatureone = new DashboardReservationDataDto();
        depatureone.setReservationId(1);
        depatureone.setCustomerName("abc alwis");
        depatureone.setContactNo("0774585695");
        depatureone.setPromisedCheckInOutDateTime(LocalDateTime.now());

        DashboardReservationDataDto depaturetwo = new DashboardReservationDataDto();
        depaturetwo.setReservationId(1);
        depaturetwo.setCustomerName("basil");
        depaturetwo.setContactNo("0774585695");
        depaturetwo.setPromisedCheckInOutDateTime(LocalDateTime.now());

        DashboardReservationDataDto depaturethree = new DashboardReservationDataDto();
        depaturethree.setReservationId(1);
        depaturethree.setCustomerName("Pasan silva");
        depaturethree.setContactNo("0774585695");
        depaturethree.setPromisedCheckInOutDateTime(LocalDateTime.now());

        checkOutList.add(depatureone);
        checkOutList.add(depaturetwo);
        checkOutList.add(depaturethree);

        dashboardDto.setExpectedCheckInListToday(checkInList);
        dashboardDto.setExpectedCheckOutListToday(checkOutList);
        return dashboardDto;
    }
}
