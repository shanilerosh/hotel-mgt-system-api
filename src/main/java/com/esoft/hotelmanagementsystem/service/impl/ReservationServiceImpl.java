package com.esoft.hotelmanagementsystem.service.impl;

import com.esoft.hotelmanagementsystem.dto.CommonResponseDto;
import com.esoft.hotelmanagementsystem.dto.ReservationCommonFilter;
import com.esoft.hotelmanagementsystem.dto.ReservationDto;
import com.esoft.hotelmanagementsystem.dto.RoomDataDto;
import com.esoft.hotelmanagementsystem.entity.CustomerMst;
import com.esoft.hotelmanagementsystem.entity.ReservationMst;
import com.esoft.hotelmanagementsystem.entity.Room;
import com.esoft.hotelmanagementsystem.entity.UserMst;
import com.esoft.hotelmanagementsystem.enums.ReservationStatus;
import com.esoft.hotelmanagementsystem.exception.CommonException;
import com.esoft.hotelmanagementsystem.repo.*;
import com.esoft.hotelmanagementsystem.service.CustomerService;
import com.esoft.hotelmanagementsystem.service.ReservationService;
import com.esoft.hotelmanagementsystem.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author ShanilErosh
 */
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ReservationServiceImpl implements ReservationService {

    private final RepositoryCustom customRepo;
    private final HotelTypeRepo hotelTypeRepo;
    private final RoomTypeRepo roomTypeRepo;
    private final RoomRepository roomRepo;
    private final RoomImgRepository roomImgRepo;
    private final ReservationRepository reservationRepository;
    private final CustomerRepo customerRepo;
    private final CustomerService customerService;
    private final UserService userService;


    @Override
    public Boolean createReservation(ReservationDto dto) {

        //validate if customer exist
        isCstomerDtoNotNull(dto);

        //get customer
        CustomerMst customerMst = getCustomerByNic(dto.getCustomerDto().getNicPass());
        //get user
        UserMst user = userService.getUser(dto.getUsername());

        var reservationMst = reservationBuilder();

        reservationMst.setCustomerMst(customerMst);
        reservationMst.setCreatedUser(user);
        reservationMst.setCreditCardApplicable(dto.isCreditCardApplicable());
        reservationMst.setReservationStatus(!dto.isCreditCardApplicable() ?
                ReservationStatus.PENDING : ReservationStatus.OPEN);


        BeanUtils.copyProperties(dto, reservationMst);

            var savedReservation = reservationRepository.save(reservationMst);

            //save room related tables
            Set<Room> rooms = dto.getRoomList().stream().map(obj -> roomRepo.findById(obj.getRoomId()).orElseThrow(() -> {
                throw new CommonException("Room not found");
            })).collect(Collectors.toSet());

            savedReservation.setTableRooms(rooms);

            reservationRepository.save(savedReservation);
            return true;
    }

    private CustomerMst getCustomerByNic(String nic) {
        return customerRepo
                .findByNicPass(nic)
                .orElseThrow(() -> {
                    throw new CommonException("Customer not found");
                });
    }

    private ReservationMst reservationBuilder() {
        ReservationMst reservation = ReservationMst.builder().build();
        reservation.setCreatedDateTime(LocalDateTime.now());

        return reservation;
    }

    @Override
    public Boolean update(Long aLong, ReservationDto dto) {
        return null;
    }

    @Override
    public ReservationDto fetchOne(Long aLong, ReservationDto dto) {
        return null;
    }

    @Override
    public Boolean createClarkReservation(ReservationDto reservationDto) {

        //validate if actual clark entered
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        //validate if customer already exist
        isCstomerDtoNotNull(reservationDto);

        boolean isCustomerExist = customerRepo
                .findByNicPass(reservationDto.getCustomerDto().getNicPass())
                .isPresent();


        if(!isCustomerExist) {
            //create new customer
            customerService.createCustomer(reservationDto.getCustomerDto(), true);
        }

        return createReservation(reservationDto);

    }

    @Override
    public CommonResponseDto<ReservationDto> fetchReservationData(ReservationCommonFilter filter, String status) {


//        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
//        String sql = "";
//        String orderDesc;
//
//
//        if(0 != filter.getNumberOfOccupants()) {
//            mapSqlParameterSource.addValue("hotelType", filter.getHotelType().trim());
//            sql += " AND LOWER(ht.hotel_name) = LOWER(:hotelType)";
//        }
//
//        if(null != filter.getRoomCategory() && !filter.getRoomCategory().isEmpty()) {
//            mapSqlParameterSource.addValue("cat", filter.getRoomCategory().trim());
//            sql += " AND LOWER(rt.room_category) = LOWER(:cat)";
//        }
//
//        if(0 != filter.getNumberOfOccupants()) {
//            mapSqlParameterSource.addValue("numberOfOccupants", filter.getNumberOfOccupants());
//            sql += " AND rt.number_of_occupants = :numberOfOccupants";
//        }
//
//        //TODO add arrival
//
//        String sqlCustom = "select rt.room_type_id, rt.room_detail, rt.room_price, rt.main_img, rt.number_of_occupants,rt.room_category from room r\n" +
//                "	left join room_type rt on r.room_type_id = rt.room_type_id\n" +
//                "	left join hotel_mst ht on ht.hotel_id= rt.hotel_id\n" +
//                "	WHERE r.house_keeping_status != 'DEACTIVE'\n";
//
//        String finalizedSql = sqlCustom.concat(sql).concat(" GROUP BY rt.room_type_id, rt.room_detail");
//        String count = "Select count(*) from room_type";
//
//
//        PageRequest pageable = PageRequest.of(filter.getPage(), filter.getSize());
//
//        Map<String, String> paramField = new HashMap<>();
//
//        paramField.put("roomTypeId","room_type_id");
//        paramField.put("roomDetail","room_detail");
//        paramField.put("roomPrice","room_price");
//        paramField.put("mainImg","main_img");
//        paramField.put("numberOfOccupants","number_of_occupants");
//        paramField.put("cat","room_category");
//
//        try {
//            Page<RoomDataDto> roomDataDtos = customRepo.executeCustomQuery(pageable, finalizedSql,
//                    mapSqlParameterSource, RoomDataDto.class, paramField, count);
//
//
//            return CommonResponseDto.<RoomDataDto>builder().data(roomDataDtos.getContent())
//                    .total(roomDataDtos.getTotalElements())
//                    .build();
//        } catch (Exception exception) {
//            exception.printStackTrace();
//            throw new RuntimeException(exception.getMessage());
//        }

        return null;
    }

    private void isCstomerDtoNotNull(ReservationDto reservationDto) {
        if(null == reservationDto.getCustomerDto()) {
            throw new CommonException("Customer data should be there to create a reservation");
        }
    }
}
