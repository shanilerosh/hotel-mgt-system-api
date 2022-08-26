package com.esoft.hotelmanagementsystem.repo;

import com.esoft.hotelmanagementsystem.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * @author ShanilErosh
 */
public interface RoleRepo extends JpaRepository<Role, Long> {

    Optional<Role> findByName(String name);
}
