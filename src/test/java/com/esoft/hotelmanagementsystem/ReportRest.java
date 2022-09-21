package com.esoft.hotelmanagementsystem;

import com.esoft.hotelmanagementsystem.dto.CommonResponseDto;
import com.esoft.hotelmanagementsystem.dto.DashboardReservationDataDto;
import com.esoft.hotelmanagementsystem.dto.ReportCommonResponseDto;
import com.esoft.hotelmanagementsystem.dto.ReportFilterDto;
import com.esoft.hotelmanagementsystem.entity.CustomerMst;
import com.esoft.hotelmanagementsystem.entity.ReservationMst;
import com.esoft.hotelmanagementsystem.entity.Role;
import com.esoft.hotelmanagementsystem.entity.UserMst;
import com.esoft.hotelmanagementsystem.enums.ReportType;
import com.esoft.hotelmanagementsystem.repo.ReservationRepository;
import com.esoft.hotelmanagementsystem.repo.RoleRepo;
import com.esoft.hotelmanagementsystem.service.impl.DashboardServiceImpl;
import com.esoft.hotelmanagementsystem.service.impl.ReportServiceImpl;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * @author ShanilErosh
 */
@SpringBootTest
public class ReportRest {

    @Autowired
    private ReportServiceImpl reportService;


    @BeforeEach
    void setup() {


    }


    @Test
    @DisplayName("Test Customer Data")
    void TestDashboardListData_Failure() {

        ReportFilterDto build = ReportFilterDto.builder().reportType(ReportType.CUSTOMER)
                .to(LocalDateTime.of(2022,8,10,1,1))
                .from(LocalDateTime.of(2022,8,10,1,1))
                .build();

        CommonResponseDto<ReportCommonResponseDto> reportCommonResponseDtoCommonResponseDto = reportService.generateReport(build);

        Assertions.assertNotNull(reportCommonResponseDtoCommonResponseDto, "Response obj cannot be null");
    }
}
