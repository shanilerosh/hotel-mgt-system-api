package com.esoft.hotelmanagementsystem.service;

import com.esoft.hotelmanagementsystem.dto.CommonResponseDto;

/**
 * @author ShanilErosh
 */
public interface CrudService<T, ID> {

    Boolean createReservation(T dto);

    Boolean update(ID id, T dto);

    T fetchOne(ID id, T dto);
}
