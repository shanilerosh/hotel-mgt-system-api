package com.esoft.hotelmanagementsystem.service;

import com.esoft.hotelmanagementsystem.dto.PaymentCalculatedDto;
import com.esoft.hotelmanagementsystem.dto.PaypalDto;

/**
 * @author ShanilErosh
 */
public interface PaymentService {

    PaymentCalculatedDto getPaymentDetail(String reservationId);
}
