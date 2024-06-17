package com.lime.minipay.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class MemberSettlement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberSettlementId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "settlementId")
    private Settlement settlement;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creditor_id", referencedColumnName = "memberId")
    private Member creditorMember;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "debtor_id", referencedColumnName = "memberId")
    private Member debtorMember;

    private Long amount;

    private Boolean isComplete = false;

    public static MemberSettlement of(Settlement settlement, Member creditorMember, Member debtorMember, Long amount) {
        MemberSettlement memberSettlement = new MemberSettlement();
        memberSettlement.settlement = settlement;
        memberSettlement.creditorMember = creditorMember;
        memberSettlement.debtorMember = debtorMember;
        memberSettlement.amount = amount;

        return memberSettlement;
    }

    public void complete() {
        this.isComplete = true;
    }
}
