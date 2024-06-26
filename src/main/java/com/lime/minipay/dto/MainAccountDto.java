package com.lime.minipay.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class MainAccountDto {
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private Long balance;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AddCashRequest {
        private Long amount;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TransferToMemberRequest {
        private Long memberId;
        private Long amount;
    }
}
