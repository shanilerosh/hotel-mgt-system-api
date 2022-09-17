package com.esoft.hotelmanagementsystem.dto;

import com.esoft.hotelmanagementsystem.enums.HouseKeepingStatus;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @author ShanilErosh
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreditCardDto {

    @NotBlank(message = "Credit card number is mandatory")
    private String creditCardNumber;
    @NotNull(message = "Expiration is mandatory")
    private LocalDate expirationDate;
    @NotBlank(message = "Card CSV number is mandatory")
    private String cardCsv;
}
