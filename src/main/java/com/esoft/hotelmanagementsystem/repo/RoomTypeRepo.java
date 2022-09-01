package com.esoft.hotelmanagementsystem.repo;

import com.esoft.hotelmanagementsystem.entity.HotelMst;
import com.esoft.hotelmanagementsystem.entity.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author ShanilErosh
 */
public interface RoomTypeRepo extends JpaRepository<RoomType, Long> {

    Optional<RoomType> findByRoomTypeId(Long id);
}
