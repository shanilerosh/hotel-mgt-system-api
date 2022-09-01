package com.esoft.hotelmanagementsystem.service;

import com.esoft.hotelmanagementsystem.dto.CommonResponseDto;
import com.esoft.hotelmanagementsystem.dto.HotelMgtCommonFilter;
import com.esoft.hotelmanagementsystem.dto.RoomDataDto;
import com.esoft.hotelmanagementsystem.entity.Role;
import com.esoft.hotelmanagementsystem.entity.UserMst;

import java.util.List;

/**
 * @author ShanilErosh
 */
public interface RoomService {

    CommonResponseDto<RoomDataDto> fetch(HotelMgtCommonFilter filter);

}
