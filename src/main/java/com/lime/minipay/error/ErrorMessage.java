package com.lime.minipay.error;

public enum ErrorMessage {
    EXCEED_CHARGE_LIMIT("충전 한도 초과");

    private String message;

    ErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}
