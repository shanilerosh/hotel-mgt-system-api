package com.esoft.hotelmanagementsystem.service.impl;

import com.esoft.hotelmanagementsystem.dto.EmailDto;
import com.esoft.hotelmanagementsystem.dto.PaymentDto;
import com.esoft.hotelmanagementsystem.dto.PaypalDto;
import com.esoft.hotelmanagementsystem.entity.CustomerMst;
import com.esoft.hotelmanagementsystem.entity.ReservationMst;
import com.esoft.hotelmanagementsystem.enums.PaymentStatus;
import com.esoft.hotelmanagementsystem.enums.PaymentType;
import com.esoft.hotelmanagementsystem.exception.CommonException;
import com.esoft.hotelmanagementsystem.repo.ReservationRepository;
import com.esoft.hotelmanagementsystem.service.PaymentService;
import com.esoft.hotelmanagementsystem.service.PaypalService;
import com.esoft.hotelmanagementsystem.util.Constants;
import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ShanilErosh
 */
@Service
@RequiredArgsConstructor
@Transactional
public class PaypalServiceImpl implements PaypalService {

    private final APIContext apiContext;
    private final PaymentService paymentService;

    @Value("${paypal.success-url}")
    private String successUrl;
    @Value("${paypal.cancel-url}")
    private String  cancelUrl;
    @Value("${queue.name-email}")
    private String queueName;
    @Value("${queue.exchange}")
    private String exchange;
    @Value("${queue.route}")
    private String routingKey;

    private final RabbitTemplate rabbitTemplate;
    private final ReservationRepository reservationRepository;


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

        redirectUrls.setCancelUrl(cancelUrl.concat(paypalDto.getReservationId()));
        redirectUrls.setReturnUrl(successUrl.concat(paypalDto.getReservationId()));

        payment.setRedirectUrls(redirectUrls);

        Payment pyMst = payment.create(apiContext);

        PaymentDto paymentDto = convertToPaymentDto(paypalDto);

        //create payment invoice in the db
        paymentService.createPaymentInvoiceRecord(paymentDto);

        for (Links link : pyMst.getLinks()) {
            if(link.getRel().equals("approval_url")) {
                return link.getHref();
            }
        }

        return null;
    }

    @Override
    public void executePayment(String reservationId, String paymentId, String payerId) throws PayPalRESTException {
        Payment payment = new Payment();
        payment.setId(paymentId);
        PaymentExecution paymentExecute = new PaymentExecution();
        paymentExecute.setPayerId(payerId);
        payment.execute(apiContext, paymentExecute);

        //mark payment as completed
        paymentService.finalizePaymentStatus(reservationId, paymentId, payerId, PaymentStatus.SUCCESS);

        sendEmailForPayment(reservationId);


    }

    private void sendEmailForPayment(String reservationId) {

        ReservationMst reservationMst = reservationRepository.findById(Long.valueOf(reservationId))
                .orElseThrow(() -> {
                    throw new CommonException("Reservation not found");
                });

        CustomerMst customerMst = reservationMst.getCustomerMst();


        //send payment email
        EmailDto emailDto = EmailDto.builder().emailBody(String.format(Constants.NEW_PAYMENT_EMAIL_BODY, customerMst.getCustomerName(),customerMst.getCustomerName(), reservationMst.getTotalAmount(),reservationId))
                .subject(String.format(Constants.NEW_CUSTOMER_EMAIL_SUBJECT, customerMst.getCustomerName()))
                .toEmail(customerMst.getUserMst().getEmail()).build();

        //push to the email que
        rabbitTemplate.convertAndSend(exchange, routingKey, emailDto);
    }

    private PaymentDto convertToPaymentDto(PaypalDto paypalDto) {

        return PaymentDto.builder().paymentAmount(paypalDto.getTotal())
                .isManualPayment(true).reservationId(paypalDto.getReservationId())
                .paymentType(PaymentType.CREDIT_CARD)
                 .laundryCharges(paypalDto.getLaundryCharges())
                         .barCharges(paypalDto.getBarCharges())
                 .telephoneCharges(paypalDto.getTelephoneCharges())
                         .clubFacility(paypalDto.getClubFacility())
                 .ketCharges(paypalDto.getKetCharges())
                .build();
    }

    public Payment executePayment(String paymentId, String payerId) throws PayPalRESTException{
        Payment payment = new Payment();
        payment.setId(paymentId);
        PaymentExecution paymentExecute = new PaymentExecution();
        paymentExecute.setPayerId(payerId);
        return payment.execute(apiContext, paymentExecute);
    }




}
