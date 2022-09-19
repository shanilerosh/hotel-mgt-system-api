package com.esoft.hotelmanagementsystem.service;

import com.esoft.hotelmanagementsystem.dto.PaymentCalculatedDto;
import com.esoft.hotelmanagementsystem.dto.PaypalDto;
import com.paypal.base.rest.PayPalRESTException;

/**
 * @author ShanilErosh
 */
public interface PaypalService {

    String createPayment(PaypalDto paypalDto) throws PayPalRESTException;

    void executePayment(String reservationId, String paymentId, String payerId) throws PayPalRESTException;
}
