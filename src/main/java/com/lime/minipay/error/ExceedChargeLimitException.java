package com.lime.minipay.error;

public class ExceedChargeLimitException extends RuntimeException {
    public ExceedChargeLimitException() {
    }

    public ExceedChargeLimitException(String message) {
        super(message);
    }
}
