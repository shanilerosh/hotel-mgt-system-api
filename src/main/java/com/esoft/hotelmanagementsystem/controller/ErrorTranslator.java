package com.esoft.hotelmanagementsystem.controller;

import com.esoft.hotelmanagementsystem.dto.ErrorDto;
import com.esoft.hotelmanagementsystem.exception.CommonException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.NativeWebRequest;

/**
 * @author ShanilErosh
 */
@Slf4j
@ControllerAdvice
public class ErrorTranslator {

    @ExceptionHandler
    public ResponseEntity<ErrorDto> handleNoSuchElementException(CommonException ex, NativeWebRequest request) {
        log.error(ex.getMessage(), ex);
        ErrorDto errorDto = new ErrorDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDto);
    }
}
