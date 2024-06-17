package com.lime.minipay.entity;

import com.lime.minipay.error.ExceedChargeLimitException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "main_account")
public class MainAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mainAccountId;

    @OneToOne
    @JoinColumn(name = "member_id", referencedColumnName = "memberId", nullable = false)
    private Member member;

    @Column(nullable = false)
    private Long balance = 0L; //잔액

    @Column(nullable = false)
    private Long dayCharged = 0L; //오늘 충전된 양

    @Column(nullable = false)
    private Long chargeLimit; //1일 충전 한도

    public static MainAccount of(Member member, Long chargeLimit) {
        MainAccount mainAccount = new MainAccount();

        mainAccount.member = member;
        mainAccount.chargeLimit = chargeLimit;

        return mainAccount;
    }

    public void addCash(Long cash) {
        //일일 충전 한도 초과 확인
        if (dayCharged + cash > chargeLimit) {
            throw new ExceedChargeLimitException("충전 한도 초과");
        }

        this.balance += cash;
        log.info("@@@: " + this.balance);
        this.dayCharged += cash;
    }

    private void withDraw(Long amount) {
        this.balance -= amount;
    }

    //송금 보내기
    public Long transferCash(Long amount) {
        if (this.balance < amount) {
            //만원단위 충전하기
            autoChargeCash(amount);
        }

        log.info("@@@TMPTMP: " + amount);
        this.withDraw(amount);

        return this.balance;
    }

    private void autoChargeCash(Long amount) {
        //TODO 오차 확인
        //자동 충전 금액 계산(만원 단위)
        Long chargeAmount = ((long) Math.ceil((double) (amount - balance) / 10_000)) * 10_000;
        log.info("자동충전: " + chargeAmount);

        addCash(chargeAmount);
    }

    public void transferCancel(Long amount) {
        receiveCash(amount);
    }

    private void receiveCash(Long amount) {
        this.balance += amount;
    }
}
