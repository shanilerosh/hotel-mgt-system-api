package com.esoft.hotelmanagementsystem.repo;

import com.esoft.hotelmanagementsystem.entity.UserMst;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author ShanilErosh
 */
@Repository
public interface UserRepo extends JpaRepository<UserMst, Long> {

    Optional<UserMst> findByUsername(String username);
}
