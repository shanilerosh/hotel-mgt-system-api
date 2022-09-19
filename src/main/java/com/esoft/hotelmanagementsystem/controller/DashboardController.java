package com.esoft.hotelmanagementsystem.controller;

import com.esoft.hotelmanagementsystem.dto.DashboardDto;
import com.esoft.hotelmanagementsystem.dto.PaymentCalculatedDto;
import com.esoft.hotelmanagementsystem.service.DashboardService;
import com.esoft.hotelmanagementsystem.service.PaymentService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {


    private final DashboardService dashboardService;

    /**
     * APi To fetch dashboard data
     *
     * @return
     */
    @GetMapping("/dashboard-data")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", required = true, dataType = "string", paramType = "header")})
    public ResponseEntity<DashboardDto> fetchDashboardData() {
        return ResponseEntity.ok().body(dashboardService.fetchDashboardData());
    }

}
