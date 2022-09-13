package com.esoft.hotelmanagementsystem.service.impl;

import com.esoft.hotelmanagementsystem.dto.*;
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

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
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
    public ReservationDto fetchOne(Long id) {

        ReservationMst reservationMst = reservationRepository.findById(id)
                .orElseThrow(() -> {
                    throw new CommonException("Reservation Not Found");
                });

        ReservationDto reservationDto = ReservationDto.builder().build();
        BeanUtils.copyProperties(reservationMst, reservationDto);

        List<RoomDto> roomDtoList = reservationMst.getTableRooms().stream().map(obj -> RoomDto.builder().roomId(obj.getRoomId())
                .houseKeepingStatus(obj.getHouseKeepingStatus())
                .isNonSmoking(obj.getIsNonSmoking())
                .roomNumber(obj.getRoomNumber())
                .roomRemark(obj.getRoomRemark())
                .build()).collect(Collectors.toList());

        reservationDto.setRoomList(roomDtoList);
        return reservationDto;
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
    public CommonResponseDto<ReservationDto> fetchReservationData(ReservationCommonFilter filter, String status, boolean isClark) {


        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        String sql = "";
        String orderDesc;

        //if it a clark all data should be show. Is not only the corresponding customer data should be displayed
        if(!isClark){
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            //TODO - Implement this when customer data is fetched

        }

        if (null != status) {
            sql = sql.concat(" AND r.reservation_status = :status");
            mapSqlParameterSource.addValue("status", status);
        }

        if(null != filter.getActualCheckedInTimeFrom() && null != filter.getActualCheckedInTimeTO()) {
            mapSqlParameterSource.addValue("actualCheckedInFrom", filter.getActualCheckedInTimeFrom());
            mapSqlParameterSource.addValue("actualCheckedInTo", filter.getActualCheckedInTimeTO());
            sql = sql.concat(" AND r.actual_checked_in_time BETWEEN :actualCheckedInFrom And :actualCheckedInTo ");
        }

        if(null != filter.getActualCheckedOutTimeFrom() && null != filter.getActualCheckedOutTimeTO()) {
            mapSqlParameterSource.addValue("actualCheckedOutFrom", filter.getActualCheckedOutTimeFrom());
            mapSqlParameterSource.addValue("actualCheckedOutTo", filter.getActualCheckedOutTimeTO());
            sql = sql.concat(" AND r.actual_checked_out_time BETWEEN :actualCheckedOutFrom And :actualCheckedOutTo ");
        }

        if(null != filter.getPromisedCheckedInTimeFrom() && null != filter.getPromisedCheckedInTimeTo()) {
            mapSqlParameterSource.addValue("promisedCheckInTimeFrom", filter.getPromisedCheckedInTimeFrom());
            mapSqlParameterSource.addValue("promisedCheckInTimeTo", filter.getPromisedCheckedInTimeTo());
            sql = sql.concat(" AND r.promised_checked_in_time BETWEEN :promisedCheckInTimeFrom And :promisedCheckInTimeTo ");
        }

        if(null != filter.getPromisedCheckedOutTimeFrom() && null != filter.getPromisedCheckedInTimeTo()) {
            mapSqlParameterSource.addValue("promisedCheckOutTimeFrom", filter.getPromisedCheckedOutTimeFrom());
            mapSqlParameterSource.addValue("promisedCheckOutTimeTo", filter.getPromisedCheckedOutTimeTo());
            sql = sql.concat(" AND r.promised_checked_out_time BETWEEN :promisedCheckOutTimeFrom And :promisedCheckOutTimeTo ");
        }

        if(null != filter.getNicPass() && !filter.getNicPass().isEmpty()) {
            mapSqlParameterSource.addValue("nicPass", filter.getNicPass());
            sql = sql.concat(" AND LOWER(c.nic_pass) LIKE LOWER(:nicPass) ");
        }

        if(null != filter.getCustomerName() && !filter.getCustomerName().isEmpty()) {
            mapSqlParameterSource.addValue("customerName", filter.getCustomerName());
            sql = sql.concat(" AND LOWER(c.customer_name) LIKE LOWER(:customerName) ");
        }


        String sqlCustom = "SELECT r.actual_checked_in_time, r.actual_checked_out_time, r.promised_checked_in_time, r.promised_checked_out_time, r.reservation_id, r.total_amount, c.customer_name, c.country, c.nic_pass, r.reservation_status FROM reservation_mst r LEFT JOIN customer_mst c\n" +
                "ON r.cust_id = c.cust_id WHERE r.reservation_id != 0 ";

        String finalizedSql = sqlCustom.concat(sql);
        String count = "SELECT count(*) FROM reservation_mst r LEFT JOIN customer_mst c\n" +
                "ON r.cust_id = c.cust_id WHERE r.reservation_id != 0 ".concat(sql);


        PageRequest pageable = PageRequest.of(filter.getPage(), filter.getSize());

        Map<String, String> paramField = new HashMap<>();

        paramField.put("actualCheckedInTime","actual_checked_in_time");
        paramField.put("actualCheckedOutTime","actual_checked_out_time");
        paramField.put("promisedCheckedInTime","promised_checked_in_time");
        paramField.put("promisedCheckedOutTime","promised_checked_out_time");
        paramField.put("reservationId","reservation_id");
        paramField.put("totalAmount","total_amount");
        paramField.put("customerName","customer_name");
        paramField.put("country","country");
        paramField.put("nicPass","nic_pass");
        paramField.put("status","reservation_status");

        try {
            Page<ReservationDto> reservationDtos = customRepo.executeCustomQuery(pageable, finalizedSql,
                    mapSqlParameterSource, ReservationDto.class, paramField, count);


            return CommonResponseDto.<ReservationDto>builder().data(reservationDtos.getContent())
                    .total(reservationDtos.getTotalElements())
                    .build();
        } catch (Exception exception) {
             exception.printStackTrace();
            throw new RuntimeException(exception.getMessage());
        }

    }

    private void isCstomerDtoNotNull(ReservationDto reservationDto) {
        if(null == reservationDto.getCustomerDto()) {
            throw new CommonException("Customer data should be there to create a reservation");
        }
    }
}
