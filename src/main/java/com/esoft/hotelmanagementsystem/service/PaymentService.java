package com.esoft.hotelmanagementsystem.service;

import com.esoft.hotelmanagementsystem.dto.PaymentCalculatedDto;
import com.esoft.hotelmanagementsystem.dto.PaymentDto;
import com.esoft.hotelmanagementsystem.dto.PaypalDto;
import com.esoft.hotelmanagementsystem.enums.PaymentStatus;

/**
 * @author ShanilErosh
 */
public interface PaymentService {

    PaymentCalculatedDto getPaymentDetail(String reservationId);

    Boolean createPaymentInvoiceRecord(PaymentDto paymentDto);

    Boolean finalizePaymentStatus(String reservationId, String paymentId, String payerId, PaymentStatus paymentStatus);
}
