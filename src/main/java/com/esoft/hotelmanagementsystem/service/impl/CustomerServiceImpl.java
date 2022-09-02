package com.esoft.hotelmanagementsystem.service.impl;

import com.esoft.hotelmanagementsystem.dto.*;
import com.esoft.hotelmanagementsystem.entity.*;
import com.esoft.hotelmanagementsystem.enums.CustomerStatus;
import com.esoft.hotelmanagementsystem.enums.HouseKeepingStatus;
import com.esoft.hotelmanagementsystem.exception.CommonException;
import com.esoft.hotelmanagementsystem.repo.*;
import com.esoft.hotelmanagementsystem.service.CustomerService;
import com.esoft.hotelmanagementsystem.service.RoomService;
import com.esoft.hotelmanagementsystem.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author ShanilErosh
 */
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepo customerRepo;
    private final UserRepo userRepo;
    private final UserService userService;

    @Override
    public Boolean createCustomer(CustomerDto customerDto) {

        try {
            //check if user exist
            userRepo.findByUsername(customerDto.getUsername())
                    .ifPresent(c -> {
                        throw new CommonException("User already exist with the username ".concat(customerDto.getUsername()));
                    });

            //check if nic passport is duplicated
            customerRepo.findByNicPass(customerDto.getNicPass())
                    .ifPresent(c -> {
                        throw new CommonException("User already exist with the username ".concat(customerDto.getUsername()));
                    });

            //create new user
            var userMst = UserMst.builder().username(customerDto.getUsername()).
                    password(customerDto.getPassword()).email(customerDto.getEmail())
                    .roles(new ArrayList<>())
                    .build();

            var savedUser = userService.saveUser(userMst);

            userService.addRoleToUser(savedUser.getUsername(), "ROLE_CUSTOMER");

            CustomerMst customerMst = CustomerMst.builder().build();

            BeanUtils.copyProperties(customerDto, customerMst);
            customerMst.setCustomerStatus(CustomerStatus.NON_PROCEEDABLE);
            customerMst.setUserMst(savedUser);

            customerRepo.save(customerMst);

            //TODO - Send Email confirmation
            return true;
        } catch (Exception exception) {
            throw exception;
        }
    }
}
