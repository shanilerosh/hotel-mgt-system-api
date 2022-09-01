package com.esoft.hotelmanagementsystem.repo;

import com.esoft.hotelmanagementsystem.entity.Room;
import com.esoft.hotelmanagementsystem.entity.RoomImg;
import com.esoft.hotelmanagementsystem.entity.RoomType;
import com.esoft.hotelmanagementsystem.enums.HouseKeepingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author ShanilErosh
 */
public interface RoomImgRepository extends JpaRepository<RoomImg, Long> {

    List<RoomImg> findAllByRoomType(RoomType type);
}
