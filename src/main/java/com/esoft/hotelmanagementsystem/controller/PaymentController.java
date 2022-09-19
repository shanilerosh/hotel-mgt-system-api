package com.esoft.hotelmanagementsystem.controller;

import com.esoft.hotelmanagementsystem.dto.PaymentCalculatedDto;
import com.esoft.hotelmanagementsystem.dto.PaypalDto;
import com.esoft.hotelmanagementsystem.service.PaymentService;
import com.esoft.hotelmanagementsystem.service.PaypalService;
import com.paypal.base.rest.PayPalRESTException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author ShanilErosh
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/payment")
public class PaymentController {


    private final PaymentService paymentService;
    private final PaypalService paypalService;

    /**
     * API to fetch payment data to get calculated at checkout
     * @param reservationId
     * @return
     */
    @GetMapping("/{reservationId}")
    public ResponseEntity<PaymentCalculatedDto> getPaymentDetail(@PathVariable String reservationId) {
        return ResponseEntity.ok().body(paymentService.getPaymentDetail(reservationId));
    }

    /**
     * API to proceed to paypal payment
     * @param paypalDto
     * @return
     */
    @PostMapping("/paypal-pay")
    public ResponseEntity<String> executePaypalPayment(@Valid @RequestBody PaypalDto paypalDto) throws PayPalRESTException {
        return ResponseEntity.ok(paypalService.createPayment(paypalDto));
    }

}
