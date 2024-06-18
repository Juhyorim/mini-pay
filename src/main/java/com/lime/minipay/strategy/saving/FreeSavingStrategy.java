package com.lime.minipay.strategy.saving;

import com.lime.minipay.entity.enums.SavingAccountType;

public class FreeSavingStrategy implements SavingStrategy {
    @Override
    public Long calculateInterest(Long principal) {
        return null;
    }

    @Override
    public boolean isTarget(SavingAccountType savingAccountType) {
        return savingAccountType == SavingAccountType.FREE;
    }
}