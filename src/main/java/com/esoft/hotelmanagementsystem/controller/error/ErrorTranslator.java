package com.esoft.hotelmanagementsystem.controller.error;


import com.esoft.hotelmanagementsystem.exception.CommonException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.NativeWebRequest;


@Slf4j
@ControllerAdvice
public class ErrorTranslator {

    @ExceptionHandler
    public ResponseEntity<ErrorDto> handleNoSuchElementException(CommonException ex, NativeWebRequest request) {
        log.error(ex.getMessage(), ex);
        ErrorDto errorDto = new ErrorDto(HttpStatus.NOT_FOUND.value(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDto);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorDto> handleNoSuchElementException(Exception ex, NativeWebRequest request) {
        log.error(ex.getMessage(), ex);
        ErrorDto errorDto = new ErrorDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), "System Error. Please contact the administrator");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDto);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorDto> handleNoSuchElementException(MethodArgumentNotValidException ex, NativeWebRequest request) {
        log.error(ex.getMessage(), ex);

        ErrorDto errorDto = new ErrorDto(HttpStatus.NOT_FOUND.value(), "System Error. Please contact the administrator");

        //bind exception to the front end
        ex.getAllErrors().stream().findFirst().ifPresent(obj -> {
            errorDto.setMessage(obj.getDefaultMessage());
        });

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDto);
    }
}
