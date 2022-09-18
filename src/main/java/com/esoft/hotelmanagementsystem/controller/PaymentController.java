package com.esoft.hotelmanagementsystem.controller;

import com.esoft.hotelmanagementsystem.dto.CustomerDto;
import com.esoft.hotelmanagementsystem.dto.PaymentCalculatedDto;
import com.esoft.hotelmanagementsystem.service.CustomerService;
import com.esoft.hotelmanagementsystem.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author ShanilErosh
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/payment")
public class PaymentController {


    private final PaymentService paymentService;

    /**
     * API to fetch payment data to get calculated at checkout
     * @param reservationId
     * @return
     */
    @GetMapping("/{reservationId}")
    public ResponseEntity<PaymentCalculatedDto> getPaymentDetail(@PathVariable String reservationId) {
        return ResponseEntity.ok().body(paymentService.getPaymentDetail(reservationId));
    }

}
