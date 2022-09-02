package com.esoft.hotelmanagementsystem.entity;

import com.esoft.hotelmanagementsystem.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @author ShanilErosh
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserMst {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String email;
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    private Collection<Role> roles = new ArrayList<>();
}
