package com.esoft.hotelmanagementsystem.repo;

import com.esoft.hotelmanagementsystem.entity.RoomImg;
import com.esoft.hotelmanagementsystem.entity.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * @author ShanilErosh
 */
public interface RoomImgRepo extends JpaRepository<RoomImg, Long> {

    List<RoomImg> findAllByRoomType(RoomType roomType);
}
