package com.esoft.hotelmanagementsystem.service;

import com.esoft.hotelmanagementsystem.dto.*;

import java.util.List;

/**
 * @author ShanilErosh
 */
public interface CustomerService {

    Boolean createCustomer(CustomerDto customerDto, Boolean isAutomated);

    CustomerDto fetchOneByNic(String nic);
}
