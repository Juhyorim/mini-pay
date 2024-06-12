package com.lime.minipay.error;

public class ForbiddenException extends RuntimeException {
    public ForbiddenException() {
        super("권한이 없습니다");
    }

    public ForbiddenException(String message) {
        super(message);
    }
}
