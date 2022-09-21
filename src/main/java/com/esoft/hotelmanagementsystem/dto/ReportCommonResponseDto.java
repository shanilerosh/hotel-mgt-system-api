package com.esoft.hotelmanagementsystem.dto;

import com.esoft.hotelmanagementsystem.enums.ReportType;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author ShanilErosh
 */

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ReportCommonResponseDto implements Serializable {

    //customer report
    private String customerName;
    private String contactNumber;
    private String country;
    private String username;
    private LocalDateTime registeredDate;
    private String nicPass;

}
