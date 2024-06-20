package com.lime.minipay.strategy.saving;

import com.lime.minipay.entity.enums.SavingAccountType;
import org.springframework.stereotype.Component;

@Component
public class FreeSavingStrategy implements SavingStrategy {
    @Override
    public Long calculateInterest(Long principal) {
        //단리 3%
        return principal * 3 / 100;
    }

    @Override
    public boolean isTarget(SavingAccountType savingAccountType) {
        return savingAccountType == SavingAccountType.FREE;
    }
}
