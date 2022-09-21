package com.esoft.hotelmanagementsystem.entity;

import com.esoft.hotelmanagementsystem.enums.CustomerStatus;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author ShanilErosh
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerMst {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long custId;
    private String nicPass;
    private String customerName;
    private String country;
    private String city;
    private String address;
    private String contactNumber;

    @Enumerated(EnumType.STRING)
    private CustomerStatus customerStatus;

    @OneToOne(targetEntity = UserMst.class)
    @JoinColumn(name = "user_id")
    private UserMst userMst;


    private LocalDateTime registeredDate;


}
