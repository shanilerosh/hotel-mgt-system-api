package com.esoft.hotelmanagementsystem.service;

import com.esoft.hotelmanagementsystem.dto.CommonResponseDto;
import com.esoft.hotelmanagementsystem.dto.ReportCommonResponseDto;
import com.esoft.hotelmanagementsystem.dto.ReportFilterDto;

/**
 * @author ShanilErosh
 */
public interface ReportService {

    CommonResponseDto<ReportCommonResponseDto> generateReport(ReportFilterDto filterDto);
}
