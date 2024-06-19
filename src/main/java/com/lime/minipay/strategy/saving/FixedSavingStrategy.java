package com.lime.minipay.strategy.saving;

import com.lime.minipay.entity.enums.SavingAccountType;

public class FixedSavingStrategy implements SavingStrategy {
    //매일 오전 8시에 가입한 금액 만큼 자동으로 출금, 이율은 단리로 5%
    @Override
    public Long calculateInterest(Long principal) {
        //단리 5%
        return principal * 5 / 100;
    }

    @Override
    public boolean isTarget(SavingAccountType savingAccountType) {
        return savingAccountType == SavingAccountType.FIXED;
    }
}
