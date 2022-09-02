package com.esoft.hotelmanagementsystem.controller;

import com.esoft.hotelmanagementsystem.dto.CustomerDto;
import com.esoft.hotelmanagementsystem.entity.Role;
import com.esoft.hotelmanagementsystem.entity.UserMst;
import com.esoft.hotelmanagementsystem.service.CustomerService;
import com.esoft.hotelmanagementsystem.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author ShanilErosh
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/customer")
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping("/")
    public ResponseEntity<Boolean> createCustomer(@RequestBody CustomerDto customerDto) {
        return ResponseEntity.ok().body(customerService.createCustomer(customerDto));
    }

}
