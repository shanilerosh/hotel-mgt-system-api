package com.esoft.hotelmanagementsystem.service.impl;

import com.esoft.hotelmanagementsystem.dto.*;
import com.esoft.hotelmanagementsystem.entity.RoomImg;
import com.esoft.hotelmanagementsystem.entity.RoomType;
import com.esoft.hotelmanagementsystem.enums.HouseKeepingStatus;
import com.esoft.hotelmanagementsystem.enums.ReportType;
import com.esoft.hotelmanagementsystem.exception.CommonException;
import com.esoft.hotelmanagementsystem.repo.*;
import com.esoft.hotelmanagementsystem.service.ReportService;
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
public class ReportServiceImpl implements ReportService {

    private final RepositoryCustom customRepo;
    private final PageRequest pageable = PageRequest.of(1, Integer.MAX_VALUE);


    @Override
    public CommonResponseDto<ReportCommonResponseDto> generateReport(ReportFilterDto filter) {

        if(filter.getReportType().equals(ReportType.CUSTOMER)) {
            return generateCustomerReport(filter);
        }
        return null;
    }

    private CommonResponseDto<ReportCommonResponseDto> generateCustomerReport(ReportFilterDto filter) {

        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        String whereSql = "";

        String selectSql = "SELECT c.customer_name, c.contact_number, c.country, u.username, c.registered_date,c.nic_pass FROM customer_mst c\n" +
                "LEFT JOIN user_mst u ON c.user_id = u.\"id\" WHERE C.cust_id != 0";
        String countSql = "SELECT count(*) FROM customer_mst c\n" +
                "LEFT JOIN user_mst u ON c.user_id = u.\"id\" WHERE C.cust_id != 0";


        if (null != filter.getFrom() && null != filter.getTo()) {
            mapSqlParameterSource.addValue("regDateFrom", filter.getFrom());
            mapSqlParameterSource.addValue("regDateTo", filter.getTo());
            whereSql = whereSql.concat(" AND c.registered_date BETWEEN :regDateFrom AND :regDateTo ");
        }

        Map<String, String> paramField = new HashMap<>();

        paramField.put("customerName", "customer_name");
        paramField.put("contactNumber", "contact_number");
        paramField.put("country", "country");
        paramField.put("username", "username");
        paramField.put("registeredDate", "registered_date");
        paramField.put("nicPass", "nic_pass");

        Page<ReportCommonResponseDto> reportCommonResponseDtos = customRepo.executeCustomQuery(pageable, selectSql.concat(whereSql),
                mapSqlParameterSource, ReportCommonResponseDto.class, paramField, countSql.concat(whereSql));

        return CommonResponseDto.<ReportCommonResponseDto>builder().data(reportCommonResponseDtos.getContent())
                .total(reportCommonResponseDtos.getTotalElements())
                .build();
    }
}
