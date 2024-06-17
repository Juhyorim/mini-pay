package com.lime.minipay.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class TransferDto {
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Getter
    public static class TransferResponse {
        private Long transferId;
        private Long remain;
    }
}
