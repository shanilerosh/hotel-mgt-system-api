package com.esoft.hotelmanagementsystem.service.impl;

import com.esoft.hotelmanagementsystem.dto.PaypalDto;
import com.esoft.hotelmanagementsystem.service.PaypalService;
import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ShanilErosh
 */
@Service
@RequiredArgsConstructor
public class PaypalServiceImpl implements PaypalService {

    private final APIContext apiContext;

    @Value("${paypal.success-url}")
    private String successUrl;
    @Value("${paypal.cancel-url}")
    private String cancelUrl;


    @Override
    public String createPayment(PaypalDto paypalDto) throws PayPalRESTException {
        Amount amount = new Amount();
        amount.setCurrency(paypalDto.getCurrency());


        BigDecimal total = paypalDto.getTotal();
        amount.setTotal(String.format("%.2f", total));

        Transaction transaction = new Transaction();
        transaction.setDescription(paypalDto.getDescription());
        transaction.setAmount(amount);

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);

        Payer payer = new Payer();
        payer.setPaymentMethod(paypalDto.getMethod());

        Payment payment = new Payment();
        payment.setIntent(paypalDto.getIntent());
        payment.setPayer(payer);
        payment.setTransactions(transactions);
        RedirectUrls redirectUrls = new RedirectUrls();

        redirectUrls.setCancelUrl(cancelUrl);
        redirectUrls.setReturnUrl(successUrl);

        payment.setRedirectUrls(redirectUrls);

        Payment pyMst = payment.create(apiContext);


        for (Links link : pyMst.getLinks()) {
            if(link.getRel().equals("approval_url")) {
                return link.getHref();
            }
        }

        return null;
    }

    public Payment executePayment(String paymentId, String payerId) throws PayPalRESTException{
        Payment payment = new Payment();
        payment.setId(paymentId);
        PaymentExecution paymentExecute = new PaymentExecution();
        paymentExecute.setPayerId(payerId);
        return payment.execute(apiContext, paymentExecute);
    }




}
