package com.esoft.hotelmanagementsystem.service.impl;

import com.esoft.hotelmanagementsystem.dto.CustomerDto;
import com.esoft.hotelmanagementsystem.dto.EmailDto;
import com.esoft.hotelmanagementsystem.entity.CustomerMst;
import com.esoft.hotelmanagementsystem.entity.UserMst;
import com.esoft.hotelmanagementsystem.enums.CustomerStatus;
import com.esoft.hotelmanagementsystem.exception.CommonException;
import com.esoft.hotelmanagementsystem.repo.CustomerRepo;
import com.esoft.hotelmanagementsystem.repo.UserRepo;
import com.esoft.hotelmanagementsystem.service.CustomerService;
import com.esoft.hotelmanagementsystem.service.UserService;
import com.esoft.hotelmanagementsystem.util.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

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
    private final RabbitTemplate rabbitTemplate;

    @Value("${queue.name-email}")
    private String queueName;
    @Value("${queue.exchange}")
    private String exchange;
    @Value("${queue.route}")
    private String routingKey;

    @Override
    public Boolean createCustomer(CustomerDto customerDto, Boolean isAutomated) {

        //check if customer duplicated by username
        isCustomerExistByUsername(customerDto);

        //check if nic passport is duplicated
        isCustomerNicDuplicated(customerDto);

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

        CustomerMst savedCustomer = customerRepo.save(customerMst);

        EmailDto emailDto = EmailDto.builder().emailBody(String.format(Constants.NEW_CUSTOMER_EMAIL_BODY, savedCustomer.getCustomerName(), savedCustomer.getCustId()))
                .subject(String.format(Constants.NEW_CUSTOMER_EMAIL_SUBJECT, savedCustomer.getCustomerName()))
                .toEmail(userMst.getEmail()).build();

        //push to the email que
        //rabbitTemplate.convertAndSend(exchange, routingKey, emailDto);

        //TODO - Send Notification

        return true;
    }

    @Override
    public CustomerDto fetchOneByNic(String nic) {

        CustomerMst customerMst = customerRepo
                .findByNicPass(nic).orElseThrow(() -> {
                    throw new CommonException("Customer not found with the nic");
                });

        CustomerDto customerDto = CustomerDto.builder().build();
        BeanUtils.copyProperties(customerMst, customerDto);

        return customerDto;
    }

    /**
     * Method to check if nic is duplicated
     *
     * @param customerDto
     */
    private void isCustomerNicDuplicated(CustomerDto customerDto) {
        customerRepo.findByNicPass(customerDto.getNicPass())
                .ifPresent(c -> {
                    throw new CommonException("User already exist with the username ".concat(customerDto.getUsername()));
                });
    }

    /**
     * Method to check if username is duplicated in the customer
     *
     * @param customerDto
     */
    private void isCustomerExistByUsername(CustomerDto customerDto) {
        //check if user exist
        userRepo.findByUsername(customerDto.getUsername())
                .ifPresent(c -> {
                    throw new CommonException("User already exist with the username ".concat(customerDto.getUsername()));
                });
    }
}
