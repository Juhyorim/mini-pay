package com.lime.minipay.entity;

import com.lime.minipay.error.ErrorMessage;
import com.lime.minipay.error.ExceedChargeLimitException;
import com.lime.minipay.error.NoMoneyEnoughException;
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

        //member의 MainAccount도 같이 초기화 되어야함
        member.addMainAccount(mainAccount);

        return mainAccount;
    }

    public void chargeCash(Long cash) {
        //일일 충전 한도 초과 확인
        if (dayCharged + cash > chargeLimit) {
            throw new ExceedChargeLimitException(ErrorMessage.EXCEED_CHARGE_LIMIT.getMessage());
        }

        addCash(cash);
        this.dayCharged += cash;
    }

    private void addCash(Long cash) {
        this.balance += cash;
    }

    private void withDraw(Long amount) {
        if (this.balance < amount) {
            throw new NoMoneyEnoughException(ErrorMessage.NO_MONEY_ENOUGH.getMessage());
        }

        this.balance -= amount;
    }

    //송금 보내기
    public Long transferCash(Long amount) {
        if (this.balance < amount) {
            //만원단위 충전하기
            autoChargeCash(amount);
        }

        this.withDraw(amount);

        return this.balance;
    }

    private void autoChargeCash(Long amount) {
        //TODO 오차 확인
        //자동 충전 금액 계산(만원 단위)
        Long chargeAmount = ((long) Math.ceil((double) (amount - balance) / 10_000)) * 10_000;
        log.info("자동충전: " + chargeAmount);

        chargeCash(chargeAmount);
    }

    public void getTransferCash(Long amount) {
        addCash(amount);
    }
}
