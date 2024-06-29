package com.lime.minipay.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.lime.minipay.entity.MainAccount;
import com.lime.minipay.entity.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
}
