package com.esoft.hotelmanagementsystem.service.impl;

import com.esoft.hotelmanagementsystem.dto.DashboardDto;
import com.esoft.hotelmanagementsystem.dto.DashboardReservationDataDto;
import com.esoft.hotelmanagementsystem.entity.ReservationMst;
import com.esoft.hotelmanagementsystem.repo.RepositoryCustom;
import com.esoft.hotelmanagementsystem.repo.ReservationRepository;
import com.esoft.hotelmanagementsystem.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final ReservationRepository reservationRepository;
    private final RepositoryCustom customRepo;


    public BigDecimal getTotalRevenue() {
        return reservationRepository.findAll().stream()
                .filter(obj -> null != obj.getActualCheckedOutTime() && obj.getActualCheckedOutTime().toLocalDate().equals(LocalDate.now()))
                .map(ReservationMst::getTotalAmount).
                reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public Long getTotalCheckins() {
        return reservationRepository.findAll().stream()
                .filter(obj -> null != obj.getActualCheckedInTime() && obj.getActualCheckedInTime().toLocalDate().equals(LocalDate.now()))
                .count();
    }

    public Long getTotalReservations(){
        return reservationRepository.findAll().stream()
                .filter(obj -> null != obj.getCreatedDateTime() && obj.getCreatedDateTime().toLocalDate().equals(LocalDate.now()))
                .count();
    }

    public Long getPromisedReservationToday() {
        return reservationRepository.findAll().stream()
                .filter(obj -> null != obj.getPromisedCheckedInTime() && obj.getPromisedCheckedInTime().toLocalDate().equals(LocalDate.now()) &&
                        null == obj.getActualCheckedInTime())
                .count();
    }

    public List<DashboardReservationDataDto> getTodayExpectedCheckinList() {
        return reservationRepository.findAll().stream().filter(obj -> null != obj.getPromisedCheckedInTime() && obj.getPromisedCheckedInTime().toLocalDate().equals(LocalDate.now()) && null == obj.getActualCheckedInTime())
                .map(obj -> DashboardReservationDataDto.builder()
                        .reservationId(obj.getReservationId())
                        .customerName(obj.getCustomerMst().getCustomerName())
                        .contactNo(obj.getCustomerMst().getContactNumber())
                        .promisedCheckInOutDateTime(obj.getPromisedCheckedInTime())
                        .build()).collect(Collectors.toList());

    }

    public List<DashboardReservationDataDto> getTodayExpectedChecOutList() {
        return reservationRepository.findAll().stream().filter(obj -> null != obj.getPromisedCheckedOutTime() && obj.getPromisedCheckedOutTime().toLocalDate().equals(LocalDate.now()) && null == obj.getActualCheckedOutTime())
                .map(obj -> DashboardReservationDataDto.builder()
                        .reservationId(obj.getReservationId())
                        .customerName(obj.getCustomerMst().getCustomerName())
                        .contactNo(obj.getCustomerMst().getContactNumber())
                        .promisedCheckInOutDateTime(obj.getPromisedCheckedOutTime())
                        .build()).collect(Collectors.toList());
    }


    @Override
    public DashboardDto fetchDashboardData() {
        DashboardDto dashboardDto = new DashboardDto();

        dashboardDto.setTotalRevenueToday(getTotalRevenue());
        dashboardDto.setTotalCheckinsToday(getTotalCheckins());
        dashboardDto.setNewReservationsToday(getTotalReservations());
        dashboardDto.setDueCheckinsToday(getPromisedReservationToday());

        //TODO - Implement this if time exist
        dashboardDto.setAvailableRooms(45);
        dashboardDto.setTotalActiveGuests(200);

        dashboardDto.setExpectedCheckInListToday(getTodayExpectedCheckinList());
        dashboardDto.setExpectedCheckOutListToday(getTodayExpectedChecOutList());
        return dashboardDto;
    }
}
