package com.esoft.hotelmanagementsystem.service;

import com.esoft.hotelmanagementsystem.dto.ReportFilterDto;

/**
 * @author ShanilErosh
 */
public interface ReportService {

    Boolean generateReport(ReportFilterDto filterDto);
}
