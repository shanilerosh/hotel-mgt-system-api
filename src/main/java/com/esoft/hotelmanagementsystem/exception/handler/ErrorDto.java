package com.esoft.hotelmanagementsystem.exception.handler;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class ErrorDto {

    private int status;
    private String message;
}
