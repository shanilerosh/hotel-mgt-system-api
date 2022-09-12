package com.esoft.hotelmanagementsystem.controller.error;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class ErrorDto {

    private int status;
    private String message;

    public ErrorDto(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
