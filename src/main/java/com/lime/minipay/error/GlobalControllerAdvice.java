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

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ErrorMessage> ForbiddenExceptionHandler(ForbiddenException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorMessage(e.getMessage()));
    }

    @ExceptionHandler(ExceedChargeLimitException.class)
    public ResponseEntity<ErrorMessage> ExceedChargeLimitExceptionHandler() {
        log.info("일일 충전한도 초과");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessage("일일 충전 한도를 초과하였습니다"));
    }
}
