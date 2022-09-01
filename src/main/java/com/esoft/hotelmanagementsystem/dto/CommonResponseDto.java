package com.esoft.hotelmanagementsystem.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author ShanilErosh
 */
@Getter
@Setter
@Builder
public class CommonResponseDto <T>{
    private List<T> data;
    private Long total;
}
