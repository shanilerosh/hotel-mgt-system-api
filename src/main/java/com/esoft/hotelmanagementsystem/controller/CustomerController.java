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

    //https://github.com/shanilerosh/hotel-mgt-system-api.git
    private final CustomerService customerService;

    @PostMapping("/")
    public ResponseEntity<Boolean> createCustomer(@RequestBody CustomerDto customerDto) {
            return ResponseEntity.ok().body(customerService.createCustomer(customerDto, false));
    }


    /**
     * API to fetch Customer by nic
     * @param nic
     * @return
     */
    @GetMapping("/{nic}")
    public ResponseEntity<CustomerDto> fetchOneCustomer(@PathVariable String nic) {
            return ResponseEntity.ok().body(customerService.fetchOneByNic(nic));
    }

}
