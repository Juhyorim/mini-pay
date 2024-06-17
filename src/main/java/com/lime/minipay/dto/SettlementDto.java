package com.lime.minipay.dto;

import com.lime.minipay.entity.Member;
import com.lime.minipay.entity.MemberSettlement;
import com.lime.minipay.entity.Settlement;
import com.lime.minipay.entity.enums.SettlementType;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class SettlementDto {
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class Create {
        private List<Long> participants;
        private Long amount;
        private SettlementType type;
    }

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class Response {
        private Long settlementId;
        private List<MemberSettlementInfo> participants;
        private Long amount;
        private Long remainAmount;
        private SettlementType type;

        public static Response of(Settlement settlement, List<MemberSettlement> memberSettlements) {
            Response response = new Response();
            response.settlementId = settlement.getSettlementId();
            response.participants = new ArrayList<>();
            response.amount = settlement.getAmount();
            response.remainAmount = settlement.getRemainAmount();
            response.type = settlement.getType();

            for (MemberSettlement memberSettlement : memberSettlements) {
                response.participants.add(MemberSettlementInfo.of(memberSettlement));
            }

            return response;
        }
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PACKAGE)
    public static class MemberSettlementInfo {
        private Long memberSettlementId;
        private Long memberId;
        private String memberName;
        private Long amount;
        private boolean isComplete;

        public static MemberSettlementInfo of(MemberSettlement memberSettlement) {
            Member debtorMember = memberSettlement.getDebtorMember();
            MemberSettlementInfo memberSettlementInfo = new MemberSettlementInfo(
                    memberSettlement.getMemberSettlementId(), debtorMember.getMemberId(),
                    debtorMember.getName(), memberSettlement.getAmount(), memberSettlement.getIsComplete());

            return memberSettlementInfo;
        }
    }
}
