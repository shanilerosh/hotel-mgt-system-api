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
@Builder
public class CustomerDto implements Serializable {

    private Long custId;
    private String nicPass;
    private String customerName;
    private String country;
    private String city;
    private String address;
    private String contactNumber;
    private String username;
    private String password;
    private String email;
}
