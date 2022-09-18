package com.esoft.hotelmanagementsystem.service;

import com.esoft.hotelmanagementsystem.dto.PaymentCalculatedDto;

/**
 * @author ShanilErosh
 */
public interface PaymentService {

    PaymentCalculatedDto getPaymentDetail(String reservationId);
}
