package com.lime.minipay.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalControllerAdvice {
    @ExceptionHandler(NoMoneyEnoughException.class)
    public ResponseEntity<ErrorMessage> NoMoneyEnoughExceptionHandler() {
        log.info("잔액 부족");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessage("잔액이 부족합니다."));
    }
}
