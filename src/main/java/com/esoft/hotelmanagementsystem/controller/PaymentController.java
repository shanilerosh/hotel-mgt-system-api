package com.esoft.hotelmanagementsystem.controller;

import com.esoft.hotelmanagementsystem.dto.PaymentCalculatedDto;
import com.esoft.hotelmanagementsystem.dto.PaypalDto;
import com.esoft.hotelmanagementsystem.enums.PaymentStatus;
import com.esoft.hotelmanagementsystem.service.PaymentService;
import com.esoft.hotelmanagementsystem.service.PaypalService;
import com.paypal.base.rest.PayPalRESTException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

/**
 * @author ShanilErosh
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/payment")
public class PaymentController {


    private final PaymentService paymentService;
    private final PaypalService paypalService;

    //success and failure end points
    @Value("${paypal.success-redirect-url}")
    private String successUrl;
    @Value("${paypal.cancel-redirect-url}")
    private String  cancelUrl;

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

    @GetMapping(value = "/payment-success/{reservationId}")
    public void handlePaypalSuccess(@PathVariable String reservationId, @RequestParam("paymentId") String paymentId,
                                                                    @RequestParam("PayerID") String payerId, HttpServletResponse response) throws IOException, PayPalRESTException {

        paypalService.executePayment(reservationId, paymentId, payerId);
        //if success redirect
        response.sendRedirect(successUrl);
    }

    @GetMapping(value = "/payment-failure/{reservationId}")
    public void handlePaypalFailure(@PathVariable String reservationId,HttpServletResponse response) throws IOException, PayPalRESTException {

        paymentService.finalizePaymentStatus(reservationId,null, null, PaymentStatus.FAILED);
        //if success redirect
        response.sendRedirect(cancelUrl);
    }

}
