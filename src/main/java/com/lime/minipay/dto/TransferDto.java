package com.lime.minipay.dto;

import com.lime.minipay.entity.Transfer;
import com.lime.minipay.entity.enums.TransferStatus;
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
        private TransferStatus status;

        public static Info of(Transfer transfer) {
            return TransferDto.Info.builder()
                    .transferId(transfer.getTransferId())
                    .toMemberId(transfer.getToAccount().getMember().getMemberId())
                    .toMemberName(transfer.getToAccount().getMember().getName())
                    .fromMemberId(transfer.getFromAccount().getMember().getMemberId())
                    .fromMemberName(transfer.getFromAccount().getMember().getName())
                    .amount(transfer.getAmount())
                    .createdAt(transfer.getCreatedAt())
                    .status(transfer.getStatus())
                    .build();
        }
    }
}
