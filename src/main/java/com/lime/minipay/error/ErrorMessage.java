package com.lime.minipay.error;

public enum ErrorMessage {
    EXCEED_CHARGE_LIMIT("충전 한도 초과"),
    NO_MONEY_ENOUGH("잔액 부족");

    private String message;

    ErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}
