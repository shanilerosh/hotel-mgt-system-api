package com.esoft.hotelmanagementsystem.dto;

import lombok.*;

import java.io.Serializable;

/**
 * @author ShanilErosh
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class EmailDto implements Serializable {
    private String toEmail;
    private String subject;
    private String emailBody;
}
