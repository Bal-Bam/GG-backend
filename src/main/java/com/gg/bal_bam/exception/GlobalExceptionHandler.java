package com.gg.bal_bam.exception;

import com.gg.bal_bam.common.ResponseTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice(basePackages = "com.gg.bal_bam")
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(CustomException.class)
    public ResponseTemplate<String> handleCustomException(CustomException e) {
        log.debug("handleCustomException : {}", e.getMessage());
        return ResponseTemplate.fail(e.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ResponseTemplate<String> handleException(Exception e) {
        log.error("handleException : {}", e.getMessage());
        return ResponseTemplate.fail(e.getMessage());
    }
}

