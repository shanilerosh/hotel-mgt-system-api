package com.esoft.hotelmanagementsystem.controller;

import com.esoft.hotelmanagementsystem.dto.*;
import com.esoft.hotelmanagementsystem.service.CustomerService;
import com.esoft.hotelmanagementsystem.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author ShanilErosh
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/report")
public class ReportController {

    private final ReportService reportService;

    @PostMapping("/")
    public ResponseEntity<CommonResponseDto<ReportCommonResponseDto>> createCustomer(@RequestBody ReportFilterDto filterDto) {
            return ResponseEntity.ok().body(reportService.generateReport(filterDto));
    }

}
