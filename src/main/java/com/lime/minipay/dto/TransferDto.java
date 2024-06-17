package com.lime.minipay.dto;

import java.time.LocalDateTime;
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

    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Getter
    public static class Info {
        private Long transferId;
        private Long amount;
        private Long fromMemberId;
        private String fromMemberName;
        private Long toMemberId;
        private String toMemberName;
        private LocalDateTime createdAt;
    }
}
