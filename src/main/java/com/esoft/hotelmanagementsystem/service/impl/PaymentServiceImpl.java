package com.esoft.hotelmanagementsystem.service.impl;

import com.esoft.hotelmanagementsystem.dto.*;
import com.esoft.hotelmanagementsystem.entity.*;
import com.esoft.hotelmanagementsystem.enums.HouseKeepingStatus;
import com.esoft.hotelmanagementsystem.enums.PaymentStatus;
import com.esoft.hotelmanagementsystem.enums.PaymentType;
import com.esoft.hotelmanagementsystem.enums.ReservationStatus;
import com.esoft.hotelmanagementsystem.exception.CommonException;
import com.esoft.hotelmanagementsystem.repo.*;
import com.esoft.hotelmanagementsystem.service.PaymentService;
import com.esoft.hotelmanagementsystem.service.ReservationService;
import com.esoft.hotelmanagementsystem.service.RoomService;
import com.esoft.hotelmanagementsystem.util.ReportUtils;
import com.esoft.hotelmanagementsystem.util.Utility;
import com.itextpdf.html2pdf.HtmlConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author ShanilErosh
 */
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    private final ReservationRepository reservationRepository;
    private final PaymentRepository paymentRepository;
    private final ReservationService reservationService;

    @Override
    public PaymentCalculatedDto getPaymentDetail(String reservationId) {

        ReservationMst reservationMst = getReservationMst(Long.valueOf(reservationId));

        //get the total amount
        LocalDateTime actualCheckedOutTime = reservationMst.getActualCheckedOutTime();
        LocalDateTime actualCheckedInTime = reservationMst.getActualCheckedInTime();

        long stayedDays = ChronoUnit.DAYS.between(actualCheckedInTime, actualCheckedOutTime);

        BigDecimal totalAmount = BigDecimal.ZERO;
        List<RoomWisePrice> roomWisePrices = new ArrayList<>();

        for (Room tableRoom : reservationMst.getTableRooms()) {
            RoomType roomType = tableRoom.getRoomType();

            RoomWisePrice roomToPrice = RoomWisePrice.builder()
                    .days(stayedDays).numberOfOccupants(roomType.getNumberOfOccupants())
                    .roomNumber(tableRoom.getRoomNumber())
                    .rowWiseAmount(roomType.getRoomPrice().multiply(BigDecimal.valueOf(stayedDays)))
                    .description(roomType.getRoomCategory().name().concat(" - " + roomType.getRoomPrice() + " X " + stayedDays))
                    .build();

            totalAmount = totalAmount.add(roomType.getRoomPrice().multiply(BigDecimal.valueOf(stayedDays)));
            roomWisePrices.add(roomToPrice);
        }

        return PaymentCalculatedDto.builder().checkedOutTime(actualCheckedOutTime).checkInTIme(actualCheckedInTime)
                .totalPayable(totalAmount).noOfDaysApplicable(stayedDays).totalPayable(totalAmount)
                .roomWisePrices(roomWisePrices).build();
    }

    /**
     * Create cash payment invoice record in the db
     * @param paymentDto
     * @return
     */
    @Override
    public Boolean createPaymentInvoiceRecord(PaymentDto paymentDto) {

        ReservationMst reservationMst = getReservationMst(Long.valueOf(paymentDto.getReservationId()));

        //validate duplicates
        paymentRepository.findByReservationMstAndPaymentStatus(reservationMst, PaymentStatus.SUCCESS)
                .ifPresent(c-> {
                    throw new CommonException("A successful payment has been already created for this reservation. Please try again");
                });

        PaymentMst paymentMst = paymentMstBuilder();

        BeanUtils.copyProperties(paymentDto, paymentMst,"paymentType","paymentDateTime");

        paymentMst.setPaymentType(paymentDto.getPaymentType());
        paymentMst.setReservationMst(reservationMst);

        paymentRepository.save(paymentMst);

        return true;
    }

    @Override
    public Boolean finalizePaymentStatus(String reservationId, String paymentId, String payerId, PaymentStatus paymentStatus) {

        ReservationMst reservationMst = getReservationMst(Long.valueOf(reservationId));

        PaymentMst paymentMst = paymentRepository.findByReservationMstAndPaymentStatus(reservationMst, PaymentStatus.OPEN)
                .orElseThrow(() -> {
                    throw new CommonException("Payment Record not found please try again");
                });

        paymentMst.setPaypalPayerId(payerId);
        paymentMst.setPaypalPaymentId(paymentId);

        paymentMst.setPaymentStatus(paymentStatus);

        paymentRepository.save(paymentMst);

        reservationMst.setReservationStatus(ReservationStatus.COMPLETED);

        reservationRepository.save(reservationMst);

        return true;
    }

    @Override
    public Boolean createCashPayment(PaymentDto paymentDto) {


        paymentDto.setPaymentType(PaymentType.CASH);
        paymentDto.setIsManualPayment(true);

        createPaymentInvoiceRecord(paymentDto);

        //finalize payment
        finalizePaymentStatus(paymentDto.getReservationId(),null, null, PaymentStatus.SUCCESS);

        return true;
    }

    @Override
    public String downloadPaymentInvoice(String reservationId) {

        //retrieve reservation
        ReservationMst reservationMst = getReservationMst(Long.valueOf(reservationId));

        PaymentMst paymentMst = paymentRepository.findByReservationMstAndPaymentStatus(reservationMst, PaymentStatus.SUCCESS)
                .orElseThrow(() -> {
                    throw new CommonException("Payment not found. Pelase try again");
                });

        String paymentInvoice = generatePaymentInvoiceByReservationAndPyMst(reservationMst, paymentMst);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        HtmlConverter.convertToPdf(paymentInvoice, byteArrayOutputStream);
        return Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray());
    }

    private String generatePaymentInvoiceByReservationAndPyMst(ReservationMst reservationMst, PaymentMst paymentMst) {

        StringBuilder paymentString = new StringBuilder(ReportUtils.paymentInvoiceStart);
        StringBuilder paymentTable = new StringBuilder();

        paymentString.append("<br>" + "<p style=\"padding: 5px;\">Customer : &emsp;&emsp;&emsp;:&emsp;" + reservationMst.getCustomerMst().getCustomerName() + "</p>" + "<p style=\"padding: 5px;\">Date&emsp;&emsp;:&emsp;" + paymentMst.getPaymentDateTime() + "</p>" + "<p style=\"padding: 5px;\">Customer Address&emsp;:&emsp;" + reservationMst.getCustomerMst().getAddress() + "</p>" + "<hr>" + "<br>");

        paymentString.append("<p style=\"padding: 5px;\">Total Amount &emsp;&emsp;&emsp;:&emsp; - " + Utility.formatCurrency(reservationMst.getTotalAmount()) + "</p>");
        paymentString.append("<p style=\"padding: 5px;\">Paid In &emsp;&emsp;&emsp;:&emsp; - " + (paymentMst.getPaymentType().equals(PaymentType.CASH) ? "Cash" : "Crdit Card")
                + "</p>");

        if(paymentMst.getPaymentType().equals(PaymentType.CREDIT_CARD)) {
            paymentString.append("<p style=\"padding: 5px;\">Paid Using &emsp;&emsp;&emsp;:&emsp; - Paypal </p>");
            paymentString.append("<p style=\"padding: 5px;\">Paypal Id &emsp;&emsp;&emsp;:&emsp; - " + paymentMst.getPaypalPaymentId() + "</p>");
            paymentString.append("<p style=\"padding: 5px;\">Paypal Payer Id &emsp;&emsp;&emsp;:&emsp; - " + paymentMst.getPaypalPayerId() + "</p>");
        }

        paymentString.append("<br />");

        PaymentCalculatedDto paymentDetail = getPaymentDetail(reservationMst.getReservationId().toString());

        //reservation table data
        paymentString.append(ReportUtils.TABLE_START);
        paymentString.append(ReportUtils.TABLE_ROW_START);

        paymentString.append(ReportUtils.TABLE_HEAD_START);
        paymentString.append("Room Number");
        paymentString.append(ReportUtils.TABLE_HEAD_END);

        paymentString.append(ReportUtils.TABLE_HEAD_START);
        paymentString.append("Days");
        paymentString.append(ReportUtils.TABLE_HEAD_END);

        paymentString.append(ReportUtils.TABLE_HEAD_START);
        paymentString.append("Description");
        paymentString.append(ReportUtils.TABLE_HEAD_END);

        paymentString.append(ReportUtils.TABLE_HEAD_START);
        paymentString.append("Total Amount");
        paymentString.append(ReportUtils.TABLE_HEAD_END);

        //add table data
        for (RoomWisePrice roomWisePrice : paymentDetail.getRoomWisePrices()) {
            paymentTable.append(ReportUtils.TABLE_ROW_START);

            addTableData(paymentTable, roomWisePrice.getRoomNumber(), roomWisePrice.getRoomNumber());
            addTableData(paymentTable, roomWisePrice.getDays(), roomWisePrice.getDays());
            addTableData(paymentTable, roomWisePrice.getDescription(), roomWisePrice.getDescription());
            addTableData(paymentTable, roomWisePrice.getRowWiseAmount(), roomWisePrice.getRowWiseAmount());

            paymentTable.append(ReportUtils.TABLE_ROW_END);
        }

        if(null != paymentMst.getLaundryCharges()){
            paymentTable.append(ReportUtils.TABLE_ROW_START);
            addTableData(paymentTable, null, null);
            addTableData(paymentTable, null, null);
            addTableData(paymentTable, "Luandry Charges", "Luandry Charges");
            addTableData(paymentTable, paymentMst.getLaundryCharges(), paymentMst.getLaundryCharges());
            paymentTable.append(ReportUtils.TABLE_ROW_END);
        }

        if(null != paymentMst.getBarCharges()){
            paymentTable.append(ReportUtils.TABLE_ROW_START);
            addTableData(paymentTable, null, null);
            addTableData(paymentTable, null, null);
            addTableData(paymentTable, "Bar Charges", "Bar Charges");
            addTableData(paymentTable, paymentMst.getBarCharges(), paymentMst.getBarCharges());
            paymentTable.append(ReportUtils.TABLE_ROW_END);
        }

        if(null != paymentMst.getTelephoneCharges()){
            paymentTable.append(ReportUtils.TABLE_ROW_START);
            addTableData(paymentTable, null, null);
            addTableData(paymentTable, null, null);
            addTableData(paymentTable, "Telephone Charges", "Telephone Charges");
            addTableData(paymentTable, paymentMst.getTelephoneCharges(), paymentMst.getTelephoneCharges());
            paymentTable.append(ReportUtils.TABLE_ROW_END);
        }

        if(null != paymentMst.getClubFacility()){
            paymentTable.append(ReportUtils.TABLE_ROW_START);
            addTableData(paymentTable, null, null);
            addTableData(paymentTable, null, null);
            addTableData(paymentTable, "Club Facility Charges", "Club Facility Charges");
            addTableData(paymentTable, paymentMst.getClubFacility(), paymentMst.getClubFacility());
            paymentTable.append(ReportUtils.TABLE_ROW_END);
        }

        if(null != paymentMst.getKetCharges()){
            paymentTable.append(ReportUtils.TABLE_ROW_START);
            addTableData(paymentTable, null, null);
            addTableData(paymentTable, null, null);
            addTableData(paymentTable, "Key Charges", "Key Charges");
            addTableData(paymentTable, paymentMst.getKetCharges(), paymentMst.getKetCharges());
            paymentTable.append(ReportUtils.TABLE_ROW_END);
        }

        paymentString.append(paymentTable);
        paymentString.append(ReportUtils.TABLE_END);
        paymentString.append("<br />");

        paymentString.append("<p style=\"padding: 5px;\"> Thank you for your payment. Wish to see you again</p>");


        return paymentString.toString();

    }

    private void addTableData(StringBuilder table, Object validate, Object value) {
        table.append(ReportUtils.TABLE_DATA_START);
        table.append(null != validate ? value : "-");
        table.append(ReportUtils.TABLE_DATA_END);
    }

    private PaymentMst paymentMstBuilder() {
        //Todo -Add logged in user data
        return PaymentMst.builder().paymentDateTime(LocalDateTime.now())
                .paymentStatus(PaymentStatus.OPEN)
                .build();
    }

    private ReservationMst getReservationMst(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> {
                    throw new CommonException("Reservation Not Found");
                });
    }
}
