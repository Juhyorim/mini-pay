package com.lime.minipay.error;

public class NoMoneyEnoughException extends RuntimeException {
    public NoMoneyEnoughException() {
        super("금액이 부족합니다.");
    }

    public NoMoneyEnoughException(String message) {
        super(message);
    }
}
