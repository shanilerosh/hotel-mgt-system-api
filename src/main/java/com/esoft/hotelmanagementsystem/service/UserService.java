package com.esoft.hotelmanagementsystem.service;

import com.esoft.hotelmanagementsystem.entity.Role;
import com.esoft.hotelmanagementsystem.entity.UserMst;

import java.util.List;

/**
 * @author ShanilErosh
 */
public interface UserService {

    UserMst saveUser(UserMst user);

    Role saveRole(Role role);

    void addRoleToUser(String username, String role);

    UserMst getUser(String username);

    List<UserMst> getUsers();
}
