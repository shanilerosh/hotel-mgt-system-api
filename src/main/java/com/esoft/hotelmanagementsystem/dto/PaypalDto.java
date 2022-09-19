package com.esoft.hotelmanagementsystem.dto;

import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author ShanilErosh
 */
@Getter
@Setter
@NoArgsConstructor
public class PaypalDto implements Serializable {
    @NotBlank(message = "Current is compulsory")
    private String currency;
    @NotBlank(message = "Method is compulsory")
    private String method;
    @NotNull(message = "Total is compulsory")
    private BigDecimal total;
    @NotBlank(message = "Intent is compulsory")
    private String intent;
    @NotBlank(message = "Description is compulsory")
    private String description;
    @NotBlank(message = "Reservation Id is compulsory")
    private String reservationId;


}
