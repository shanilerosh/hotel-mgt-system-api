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
        }else if(filter.getReportType().equals(ReportType.REVENUE)) {
            return generateRevenueReport(filter);
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


    private CommonResponseDto<ReportCommonResponseDto> generateRevenueReport(ReportFilterDto filter) {

        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        String whereSql = "";

        String selectSql = "SELECT p.payment_amount,p.bar_charges,p.club_facility,p.ket_charges,p.laundry_charges" +
                ",p.telephone_charges,r.reservation_id,c.customer_name,c.address,r.actual_checked_out_time,p.payment_date_time FROM payment_mst p LEFT JOIN reservation_mst r on r.reservation_id = p.payment_id LEFT JOIN customer_mst c ON c.cust_id = r.cust_id WHERE p.payment_id != 0";
        String countSql = "SELECT count(*) FROM payment_mst p LEFT JOIN reservation_mst r on r.reservation_id = p.payment_id LEFT JOIN customer_mst c ON c.cust_id = r.cust_id WHERE p.payment_id != 0 ";


        if (null != filter.getFrom() && null != filter.getTo()) {
            mapSqlParameterSource.addValue("paymentDateFrom", filter.getFrom());
            mapSqlParameterSource.addValue("paymentDateTo", filter.getTo());
            whereSql = whereSql.concat(" AND p.payment_date_time BETWEEN :paymentDateFrom AND :paymentDateTo ");
        }

        Map<String, String> paramField = new HashMap<>();

        paramField.put("customerName", "customer_name");
        paramField.put("laundryCharges", "laundry_charges");
        paramField.put("barCharges", "bar_charges");
        paramField.put("telephoneCharges", "telephone_charges");
        paramField.put("clubFacility", "club_facility");
        paramField.put("ketCharges", "ket_charges");
        paramField.put("reservationId", "reservation_id");
        paramField.put("customerAddress", "address");
        paramField.put("actualCheckoutOutDate", "actual_checkout_out_time");
        paramField.put("paymentDateTime", "payment_date_time");

        Page<ReportCommonResponseDto> reportCommonResponseDtos = customRepo.executeCustomQuery(pageable, selectSql.concat(whereSql),
                mapSqlParameterSource, ReportCommonResponseDto.class, paramField, countSql.concat(whereSql));

        return CommonResponseDto.<ReportCommonResponseDto>builder().data(reportCommonResponseDtos.getContent())
                .total(reportCommonResponseDtos.getTotalElements())
                .build();
    }
}
