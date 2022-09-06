package com.esoft.hotelmanagementsystem.service.impl;

import com.esoft.hotelmanagementsystem.entity.Role;
import com.esoft.hotelmanagementsystem.entity.UserMst;
import com.esoft.hotelmanagementsystem.exception.CommonException;
import com.esoft.hotelmanagementsystem.repo.RoleRepo;
import com.esoft.hotelmanagementsystem.repo.UserRepo;
import com.esoft.hotelmanagementsystem.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author ShanilErosh
 */
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserMst saveUser(UserMst user) {
        try {
            log.info("Saving new {} user to the db", user.getUsername());
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            return userRepo.save(user);
        } catch (Exception exception) {
            throw exception;
        }
    }

    @Override
    public Role saveRole(Role role) {

        roleRepo.findByName(role.getName())
                .ifPresent(c -> {
                    throw new CommonException("Role Already Exist with the name "+ role.getName());
                });

        return roleRepo.save(role);
    }

    @Override
    public void addRoleToUser(String username, String role) {
        UserMst user = userRepo.findByUsername(username)
                .orElseThrow(() -> {
                    throw new CommonException("Error");
                });

        Role roleMst = roleRepo.findByName(role)
                .orElseThrow(() -> {
                    throw new CommonException("Error");
                });

        user.getRoles().add(roleMst);

        saveUser(user);
    }

    @Override
    public UserMst getUser(String username) {
        return userRepo.findByUsername(username)
                .orElseThrow(() -> {
                    throw new CommonException("User not exist with username "+username);
                });
    }

    @Override
    public List<UserMst> getUsers() {
        return userRepo.findAll();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserMst user = getUser(username);
        Collection<SimpleGrantedAuthority> auths= new ArrayList<>();
        user.getRoles().forEach(role -> auths.add(new SimpleGrantedAuthority(role.getName())));
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), auths);
    }
}
