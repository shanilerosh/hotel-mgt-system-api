package com.esoft.hotelmanagementsystem;

import com.esoft.hotelmanagementsystem.dto.DashboardReservationDataDto;
import com.esoft.hotelmanagementsystem.entity.CustomerMst;
import com.esoft.hotelmanagementsystem.entity.ReservationMst;
import com.esoft.hotelmanagementsystem.entity.Role;
import com.esoft.hotelmanagementsystem.entity.UserMst;
import com.esoft.hotelmanagementsystem.repo.ReservationRepository;
import com.esoft.hotelmanagementsystem.repo.RoleRepo;
import com.esoft.hotelmanagementsystem.service.impl.DashboardServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * @author ShanilErosh
 */
@SpringBootTest
public class DashboardServiceTest {

    @MockBean
    private ReservationRepository reservationRepository;

    private ReservationMst testReservation;
    private ReservationMst testReservation2;

    @Autowired
    private DashboardServiceImpl dashboardService;

    @MockBean
    private RoleRepo roleRepo;

    private UserMst testUser;

    private Role testRole;

    public static final String VALID_USER_NAME = "prathiba";
    public static final String VALID_ROLE = "ROLE_TEST";

    @BeforeEach
    void setup() {

        testReservation = ReservationMst.builder().totalAmount(BigDecimal.valueOf(200L)).
                reservationId(1L)
                .actualCheckedOutTime(LocalDateTime.now())
                .createdDateTime(LocalDateTime.now())
                .actualCheckedInTime(LocalDateTime.now())
                .promisedCheckedInTime(LocalDateTime.now())
                .promisedCheckedOutTime(LocalDateTime.now())
                .actualCheckedOutTime(LocalDateTime.now())
                .customerMst(CustomerMst.builder().customerName("Shanil").contactNumber("077631109").build())
                .build();
        testReservation2 = ReservationMst.builder().totalAmount(BigDecimal.valueOf(200L)).
                reservationId(2L)
                .actualCheckedOutTime(LocalDateTime.now())
                .createdDateTime(LocalDateTime.now())
                .actualCheckedInTime(LocalDateTime.now())
                .promisedCheckedInTime(LocalDateTime.now())
                .promisedCheckedOutTime(LocalDateTime.now())
                .actualCheckedOutTime(LocalDateTime.now())
                .customerMst(CustomerMst.builder().customerName("Tharindu").contactNumber("077631109").build())
                .build();

        //mocking reservation repo
        when(reservationRepository.findAll()).thenReturn(Arrays.asList(testReservation, testReservation2));

        when(reservationRepository.save(any(ReservationMst.class))).thenReturn(testReservation);

    }
    @Test
    @DisplayName("Test Revenue Count Sucess")
    void TestDashboardTotalRevenue() {

        BigDecimal totalRevenue = dashboardService.getTotalRevenue();
        Assertions.assertEquals(BigDecimal.valueOf(400L), totalRevenue);
    }


    @Test
    @DisplayName("Test Total Checking Today Success")
    void TestDashboardTotalTodayChecking_Success() {

        Long totalCheckins = dashboardService.getTotalCheckins();

        Assertions.assertEquals(totalCheckins, 2);
    }

    @Test
    @DisplayName("Test Total Checking Today Failure")
    void TestDashboardTotalTodayChecking_Failure() {

        testReservation.setActualCheckedInTime(LocalDateTime.now().plusDays(5));
        reservationRepository.save(testReservation);

        Long totalCheckins = dashboardService.getTotalCheckins();

        Assertions.assertNotEquals(totalCheckins, 2);
    }

    @Test
    @DisplayName("Test Total Checkins")
    void TestDashboardTotalRevenue_Failure() {

        testReservation.setActualCheckedOutTime(LocalDateTime.now().plusDays(4L));

        reservationRepository.save(testReservation);

        BigDecimal totalRevenue = dashboardService.getTotalRevenue();
        Assertions.assertNotEquals(BigDecimal.valueOf(400L), totalRevenue);
    }

    @Test
    @DisplayName("Test Total Reservation Today Success")
    void TestDashboardTotalTodayReservation_Success() {

        Long totalReservations = dashboardService.getTotalReservations();

        Assertions.assertEquals(totalReservations, 2);
    }

    @Test
    @DisplayName("Test Total Reservation Today Failure")
    void TestDashboardTotalTodayReservation_Failure() {

        testReservation.setCreatedDateTime(LocalDateTime.now().plusDays(5));

        reservationRepository.save(testReservation);

        Long totalReservations = dashboardService.getTotalReservations();

        Assertions.assertNotEquals(totalReservations, 2);
    }

    @Test
    @DisplayName("Test Total Reservation Promised Today Success")
    void TestDashboardTotalTodayPromisedReservation_Success() {

        Long totalReservations = dashboardService.getPromisedReservationToday();

        Assertions.assertEquals(totalReservations, 0);
    }

    @Test
    @DisplayName("Test Total Reservation Today Failure")
    void TestDashboardTotalTodayPromisedReservation_Failure() {

        testReservation.setActualCheckedInTime(null);

        reservationRepository.save(testReservation);

        Long totalReservations = dashboardService.getPromisedReservationToday();

        Assertions.assertEquals(totalReservations, 1);
    }

    @Test
    @DisplayName("Test Total Expected Check In List - No Data")
    void TestDashboardTotalTodayExpectedChekins_NoData() {

        List<DashboardReservationDataDto> todayExpectedCheckinList = dashboardService.getTodayExpectedCheckinList();

        Assertions.assertEquals(todayExpectedCheckinList.size(), 0);
    }

    @Test
    @DisplayName("Test Total Expected Check In List - With Data")
    void TestDashboardTotalTodayExpectedChekins_WithData() {

        testReservation.setActualCheckedInTime(null);
        reservationRepository.save(testReservation);

        List<DashboardReservationDataDto> todayExpectedCheckinList = dashboardService.getTodayExpectedCheckinList();

        Assertions.assertEquals(todayExpectedCheckinList.size(), 1);
    }


    @Test
    @DisplayName("Test Total Expected Check Out List - No Data")
    void TestDashboardTotalTodayExpectedCheckOut_NoData() {

        List<DashboardReservationDataDto> todayExpectedCheckinList = dashboardService.getTodayExpectedChecOutList();

        Assertions.assertEquals(todayExpectedCheckinList.size(), 0);
    }

    @Test
    @DisplayName("Test Total Expected Check Out List - With Data")
    void TestDashboardTotalTodayExpectedCheckOut_WithData() {

        testReservation.setActualCheckedOutTime(null);
        reservationRepository.save(testReservation);

        List<DashboardReservationDataDto> todayExpectedCheckinList = dashboardService.getTodayExpectedChecOutList();

        Assertions.assertEquals(todayExpectedCheckinList.size(), 1);
    }

    @Test
    @DisplayName("Test DataSet")
    void TestDashboardListData() {

        testReservation.setActualCheckedOutTime(null);
        reservationRepository.save(testReservation);

        List<DashboardReservationDataDto> todayExpectedCheckinList = dashboardService.getTodayExpectedChecOutList();

        for (DashboardReservationDataDto dashboardReservationDataDto : todayExpectedCheckinList) {
            Assertions.assertNotNull(dashboardReservationDataDto, "Dashboard obj Cannot be null");
            Assertions.assertNotNull(dashboardReservationDataDto.getCustomerName(), "Customer Name cannot be null");
            Assertions.assertNotNull(dashboardReservationDataDto.getContactNo(), "Contact Number cannot be null");
        }
    }

    @Test
    @DisplayName("Test DataSet_Failue")
    void TestDashboardListData_Failure() {

        testReservation.setActualCheckedOutTime(null);
        testReservation.setCustomerMst(CustomerMst.builder().customerName(null).build());
        reservationRepository.save(testReservation);

        List<DashboardReservationDataDto> todayExpectedCheckinList = dashboardService.getTodayExpectedChecOutList();

        for (DashboardReservationDataDto dashboardReservationDataDto : todayExpectedCheckinList) {
            Assertions.assertNull(dashboardReservationDataDto.getCustomerName(), "Customer Name should be null");
        }
    }
}
