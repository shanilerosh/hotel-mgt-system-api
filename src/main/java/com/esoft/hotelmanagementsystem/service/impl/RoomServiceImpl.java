package com.esoft.hotelmanagementsystem.service.impl;

import com.esoft.hotelmanagementsystem.dto.CommonResponseDto;
import com.esoft.hotelmanagementsystem.dto.HotelMgtCommonFilter;
import com.esoft.hotelmanagementsystem.dto.RoomDataDto;
import com.esoft.hotelmanagementsystem.entity.Role;
import com.esoft.hotelmanagementsystem.entity.UserMst;
import com.esoft.hotelmanagementsystem.exception.CommonException;
import com.esoft.hotelmanagementsystem.repo.RepositoryCustom;
import com.esoft.hotelmanagementsystem.repo.RoleRepo;
import com.esoft.hotelmanagementsystem.repo.UserRepo;
import com.esoft.hotelmanagementsystem.service.RoomService;
import com.esoft.hotelmanagementsystem.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author ShanilErosh
 */
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class RoomServiceImpl implements RoomService {

    private final RepositoryCustom customRepo;

    @Override
    public CommonResponseDto<RoomDataDto> fetch(HotelMgtCommonFilter filter) {

        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        String sql = "";
        String orderDesc;


        if(null != filter.getHotelType() && !filter.getHotelType().isEmpty()) {
            mapSqlParameterSource.addValue("hotelType", filter.getHotelType().trim());
            sql += " AND LOWER(ht.hotel_name) = LOWER(:hotelType)";
        }

        if(null != filter.getRoomCategory() && !filter.getRoomCategory().isEmpty()) {
            mapSqlParameterSource.addValue("cat", filter.getRoomCategory().trim());
            sql += " AND LOWER(rt.room_category) = LOWER(:cat)";
        }

        if(0 != filter.getNumberOfOccupants()) {
            mapSqlParameterSource.addValue("numberOfOccupants", filter.getNumberOfOccupants());
            sql += " AND rt.number_of_occupants = :numberOfOccupants";
        }

        //TODO add arrival

        String sqlCustom = "select rt.room_type_id, rt.room_detail, rt.room_price, rt.main_img, rt.number_of_occupants,rt.room_category from room r\n" +
                "	left join room_type rt on r.room_type_id = rt.room_type_id\n" +
                "	left join hotel_mst ht on ht.hotel_id= rt.hotel_id\n" +
                "	WHERE r.house_keeping_status != 'DEACTIVE'\n";

        String finalizedSql = sqlCustom.concat(sql).concat(" GROUP BY rt.room_type_id, rt.room_detail");
        String count = "Select count(*) from room_type";


        PageRequest pageable = PageRequest.of(filter.getPage(), filter.getSize());

        Map<String, String> paramField = new HashMap<>();

        paramField.put("roomTypeId","room_type_id");
        paramField.put("roomDetail","room_detail");
        paramField.put("roomPrice","room_price");
        paramField.put("mainImg","main_img");
        paramField.put("numberOfOccupants","number_of_occupants");
        paramField.put("cat","room_category");

        try {
            Page<RoomDataDto> roomDataDtos = customRepo.executeCustomQuery(pageable, finalizedSql,
                    mapSqlParameterSource, RoomDataDto.class, paramField, count);


            return CommonResponseDto.<RoomDataDto>builder().data(roomDataDtos.getContent())
                    .total(roomDataDtos.getTotalElements())
                    .build();
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new RuntimeException(exception.getMessage());
        }
    }
}
