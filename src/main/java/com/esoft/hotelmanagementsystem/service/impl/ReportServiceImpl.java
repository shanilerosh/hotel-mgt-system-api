package com.esoft.hotelmanagementsystem.service.impl;

import com.esoft.hotelmanagementsystem.dto.*;
import com.esoft.hotelmanagementsystem.entity.RoomImg;
import com.esoft.hotelmanagementsystem.entity.RoomType;
import com.esoft.hotelmanagementsystem.enums.HouseKeepingStatus;
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
    private final HotelTypeRepo hotelTypeRepo;
    private final RoomTypeRepo roomTypeRepo;
    private final RoomRepository roomRepo;
    private final RoomImgRepository roomImgRepo;


    @Override
    public Boolean generateReport(ReportFilterDto filterDto) {
        return null;
    }
}
