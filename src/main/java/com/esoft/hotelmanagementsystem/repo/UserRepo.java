package com.esoft.hotelmanagementsystem.repo;

import com.esoft.hotelmanagementsystem.entity.UserMst;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author ShanilErosh
 */
public interface UserRepo extends JpaRepository<UserMst, Long> {

    Optional<UserMst> findByUsername(String username);
}
