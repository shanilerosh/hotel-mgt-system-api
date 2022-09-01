package com.esoft.hotelmanagementsystem.repo;

import com.esoft.hotelmanagementsystem.entity.Room;
import com.esoft.hotelmanagementsystem.entity.RoomType;
import com.esoft.hotelmanagementsystem.enums.HouseKeepingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * @author ShanilErosh
 */
public interface RoomRepository extends JpaRepository<Room, Long> {

    List<Room> findByRoomTypeAndHouseKeepingStatusNot(RoomType roomType, HouseKeepingStatus status);
}
