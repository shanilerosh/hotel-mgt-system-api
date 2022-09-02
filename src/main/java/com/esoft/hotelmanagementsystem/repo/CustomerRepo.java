package com.esoft.hotelmanagementsystem.repo;

import com.esoft.hotelmanagementsystem.entity.CustomerMst;
import com.esoft.hotelmanagementsystem.entity.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author ShanilErosh
 */
public interface CustomerRepo extends JpaRepository<CustomerMst, Long> {

    Optional<CustomerMst> findByNicPass(String nic);
}
