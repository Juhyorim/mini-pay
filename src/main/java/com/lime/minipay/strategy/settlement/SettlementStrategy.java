package com.lime.minipay.strategy.settlement;

import com.lime.minipay.DividedMoneys;
import com.lime.minipay.entity.enums.SettlementType;

public interface SettlementStrategy {
    DividedMoneys divide(Long amount, int cnt);

    boolean isTarget(SettlementType settlementType);
}
