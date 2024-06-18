package com.lime.minipay.strategy.saving;

import com.lime.minipay.entity.enums.SavingAccountType;

public interface SavingStrategy {
    Long calculateInterest(Long principal);

    boolean isTarget(SavingAccountType savingAccountType);
}
