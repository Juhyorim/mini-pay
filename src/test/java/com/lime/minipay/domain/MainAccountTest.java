package com.lime.minipay.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.lime.minipay.entity.MainAccount;
import com.lime.minipay.entity.Member;
import com.lime.minipay.error.ErrorMessage;
import com.lime.minipay.error.ExceedChargeLimitException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class MainAccountTest {
    private static final Long DEFAULT_CHARGE_LIMIT = 3_000_000L;

    @Test
    @DisplayName("MainAccount 생성 테스트")
    void createMainAccount() {
        //given
        Member member = Member.createMember("loginId", "password", "라임");

        //when
        MainAccount mainAccount = MainAccount.of(member, DEFAULT_CHARGE_LIMIT);

        //then
        assertThat(mainAccount.getMember().getLoginId()).isEqualTo(member.getLoginId());
        assertThat(mainAccount.getMember().getPassword()).isEqualTo(member.getPassword());
        assertThat(mainAccount.getMember().getName()).isEqualTo(member.getName());
        assertThat(mainAccount.getBalance()).isEqualTo(0L);
        assertThat(mainAccount.getDayCharged()).isEqualTo(0L);
        assertThat(mainAccount.getChargeLimit()).isEqualTo(DEFAULT_CHARGE_LIMIT);
    }

    @ParameterizedTest
    @CsvSource(value = {"1", "10000", "100000", "3000000"})
    @DisplayName("1. 충전 테스트 - 정상동작")
    void chargeCash(long amount) {
        //given
        Member member = Member.createMember("loginId", "password", "라임");
        MainAccount mainAccount = MainAccount.of(member, DEFAULT_CHARGE_LIMIT);

        //when
        mainAccount.addCash(amount);

        //then
        assertThatNoException();
        assertThat(mainAccount.getBalance()).isEqualTo(amount);
    }

    @Test
    @DisplayName("2. 충전 테스트 - 예외발생")
    void chargeCashWithException() {
        //given
        Member member = Member.createMember("loginId", "password", "라임");
        MainAccount mainAccount = MainAccount.of(member, DEFAULT_CHARGE_LIMIT);

        //when
        //then
        assertThatThrownBy(() -> {
            mainAccount.addCash(1L);
            mainAccount.addCash(DEFAULT_CHARGE_LIMIT);
        })
                .isInstanceOf(ExceedChargeLimitException.class)
                .hasMessageContaining(ErrorMessage.EXCEED_CHARGE_LIMIT.getMessage());
    }

    @ParameterizedTest
    @CsvSource(value = {"1", "10000", "100000", "3000000"})
    @DisplayName("1. 인출 테스트 - 정상 동작")
    void withDraw(Long amount) {
        //given
        Member member = Member.createMember("loginId", "password", "라임");
        MainAccount mainAccount = MainAccount.of(member, DEFAULT_CHARGE_LIMIT);
        mainAccount.addCash(DEFAULT_CHARGE_LIMIT);

        //when
        mainAccount.transferCash(amount);

        //then
        assertThat(mainAccount.getBalance()).isEqualTo(DEFAULT_CHARGE_LIMIT - amount);
    }

    @ParameterizedTest
    @CsvSource(value = {"100:10000", "10000:10000", "100000:100000", "12345:20000"}, delimiter = ':')
    @DisplayName("2. 인출 테스트 - 자동 충전")
    void withDrawWithAutoCharge(Long amount, Long chargeAmount) {
        //given
        Member member = Member.createMember("loginId", "password", "라임");
        MainAccount mainAccount = MainAccount.of(member, DEFAULT_CHARGE_LIMIT);

        //when
        mainAccount.transferCash(amount);

        //then
        assertThat(mainAccount.getBalance()).isEqualTo(chargeAmount - amount);
    }
}
